package com.clemente.cafecampusclemente.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clemente.cafecampusclemente.data.prefs.PreferenciasStore
import com.clemente.cafecampusclemente.data.repository.CafeRepository
import com.clemente.cafecampusclemente.data.local.entity.Usuario
import com.clemente.cafecampusclemente.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: CafeRepository,
    private val preferencias: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> =
        _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso: StateFlow<Boolean> =
        _registroExitoso.asStateFlow()

    val modoOscuro: StateFlow<Boolean> =
        preferencias.modoOscuro.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val recibirPedidos: StateFlow<Boolean> =
        preferencias.recibirPedidos.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun login(
        correo: String,
        clave: String,
        rolSeleccionado: Rol
    ) {
        viewModelScope.launch {
            _error.value = null

            if (correo.isBlank() || clave.isBlank()) {
                _error.value = "Completa todos los campos"
                return@launch
            }

            val usuario = repo.login(
                correo.trim(),
                clave
            )

            if (usuario == null) {
                _error.value = "Correo o contraseña incorrectos"
                return@launch
            }

            if (usuario.rol != rolSeleccionado.name) {
                _error.value =
                    "El usuario no corresponde al rol seleccionado"
                return@launch
            }

            preferencias.guardarPerfil(usuario.rol)
            _usuarioActual.value = usuario
        }
    }

    fun cerrarError() {
        _error.value = null
    }

    fun resetRegistro() {
        _registroExitoso.value = false
        _error.value = null
    }

    fun registrarEstudiante(
        nombre: String,
        correo: String,
        clave: String
    ) {
        viewModelScope.launch {
            _error.value = null
            _registroExitoso.value = false

            if (nombre.isBlank() || correo.isBlank() || clave.isBlank()) {
                _error.value = "Completa todos los campos"
                return@launch
            }

            val correoLimpio = correo.trim()
            if (!correoLimpio.contains("@")) {
                _error.value = "Ingresa un correo electrónico válido"
                return@launch
            }

            // Valida que el correo sea único para evitar duplicados.
            val existente = repo.usuarioPorCorreo(correoLimpio)
            if (existente != null) {
                _error.value = "El correo ya está registrado"
                return@launch
            }

            val usuario = Usuario(
                nombre = nombre.trim(),
                correo = correoLimpio,
                clave = clave,
                rol = Rol.CLIENTE.name
            )

            repo.registrarUsuario(usuario)
            _registroExitoso.value = true
        }
    }

    // Alterna entre ADMIN y CLIENTE sin volver a pedir credenciales.
    // Devuelve el rol objetivo para que la UI pueda navegar de inmediato.
    fun cambiarPerfil(): Rol? {
        val actual = _usuarioActual.value ?: return null

        val rolObjetivo = if (actual.rol == Rol.ADMIN.name) {
            Rol.CLIENTE
        } else {
            Rol.ADMIN
        }

        viewModelScope.launch {
            val usuario = repo.usuarioPorRol(rolObjetivo.name)
            if (usuario != null) {
                preferencias.guardarPerfil(usuario.rol)
                _usuarioActual.value = usuario
            }
        }

        return rolObjetivo
    }


    fun cambiarModoOscuro(activado: Boolean) {
        viewModelScope.launch {
            preferencias.guardarModoOscuro(activado)
        }
    }

    fun cambiarRecepcionPedidos(activado: Boolean) {
        viewModelScope.launch {
            preferencias.guardarRecibirPedidos(activado)
        }
    }

    fun cerrarSesion() {
        // Limpia el estado de forma síncrona para evitar que la pantalla de
        // login detecte al usuario anterior y vuelva a navegar automáticamente.
        _usuarioActual.value = null
        viewModelScope.launch {
            preferencias.cerrarSesion()
        }
    }
}