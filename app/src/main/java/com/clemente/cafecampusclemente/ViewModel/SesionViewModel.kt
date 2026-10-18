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

    val modoOscuro: StateFlow<Boolean> =
        preferencias.modoOscuro.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
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
}