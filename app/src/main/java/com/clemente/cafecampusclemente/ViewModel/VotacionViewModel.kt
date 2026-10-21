package com.clemente.cafecampusclemente.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clemente.cafecampusclemente.data.local.entity.Producto
import com.clemente.cafecampusclemente.data.local.entity.Voto
import com.clemente.cafecampusclemente.data.repository.CafeRepository
import com.clemente.cafecampusclemente.model.ProductoVotos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class VotacionViewModel(
    private val repo: CafeRepository
) : ViewModel() {

    val productos: StateFlow<List<Producto>> =
        repo.observarProductos().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private fun inicioDelDia(): Long =
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    val votosPorProducto: StateFlow<List<ProductoVotos>> =
        repo.observarVotosPorProducto(inicioDelDia())
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    private val _votosEstudiante = MutableStateFlow(0)
    val votosEstudiante: StateFlow<Int> = _votosEstudiante.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun observarEstudiante(estudianteId: Int) {
        viewModelScope.launch {
            repo.observarVotosEstudiante(estudianteId, inicioDelDia())
                .collect { _votosEstudiante.value = it }
        }
    }

    fun votar(estudianteId: Int, productoId: Int) {
        viewModelScope.launch {
            _error.value = null

            if (estudianteId <= 0) {
                _error.value = "Inicia sesión para votar"
                return@launch
            }

            val yaVoto = repo.votosDelEstudiante(
                estudianteId,
                inicioDelDia()
            )
            if (yaVoto > 0) {
                _error.value = "Ya votaste hoy"
                return@launch
            }

            repo.registrarVoto(
                Voto(
                    estudianteId = estudianteId,
                    productoId = productoId
                )
            )
        }
    }
}
