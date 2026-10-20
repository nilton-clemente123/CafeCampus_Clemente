package com.clemente.cafecampusclemente.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clemente.cafecampusclemente.data.repository.CafeRepository
import com.clemente.cafecampusclemente.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

class PanelAdminViewModel(
    private val repo: CafeRepository
) : ViewModel() {

    val nuevos: StateFlow<Int> =
        repo.observarCantidadPorEstado(EstadoPedido.RECIBIDO)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                0
            )

    val preparando: StateFlow<Int> =
        repo.observarCantidadPorEstado(EstadoPedido.PREPARANDO)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                0
            )

    val listos: StateFlow<Int> =
        repo.observarCantidadPorEstado(EstadoPedido.LISTO)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                0
            )

    private fun inicioDelDia(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    val ventasHoy: StateFlow<Double> =
        repo.observarVentasHoy(inicioDelDia())
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                0.0
            )
}
