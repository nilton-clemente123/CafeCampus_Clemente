package com.clemente.cafecampusclemente.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clemente.cafecampusclemente.data.repository.CafeRepository
import com.clemente.cafecampusclemente.model.EstadoPedido
import com.clemente.cafecampusclemente.data.local.relation.PedidoConDetalle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PedidosAdminViewModel(
    private val repo: CafeRepository
) : ViewModel() {

    private val _estadoSeleccionado =
        MutableStateFlow(EstadoPedido.RECIBIDO)

    val estadoSeleccionado: StateFlow<EstadoPedido> =
        _estadoSeleccionado.asStateFlow()

    val pedidos: StateFlow<List<PedidoConDetalle>> =
        _estadoSeleccionado
            .flatMapLatest { estado ->
                repo.observarPedidosPorEstado(estado)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun seleccionarEstado(estado: EstadoPedido) {
        _estadoSeleccionado.value = estado
    }

    fun avanzar(pedido: PedidoConDetalle) {
        val estadoActual = runCatching {
            EstadoPedido.valueOf(pedido.pedido.estado)
        }.getOrNull() ?: return

        val siguienteEstado = when (estadoActual) {
            EstadoPedido.RECIBIDO -> EstadoPedido.PREPARANDO
            EstadoPedido.PREPARANDO -> EstadoPedido.LISTO
            EstadoPedido.LISTO -> EstadoPedido.ENTREGADO
            EstadoPedido.ENTREGADO,
            EstadoPedido.CANCELADO -> null
        } ?: return

        viewModelScope.launch {
            repo.cambiarEstado(
                pedidoId = pedido.pedido.id,
                estado = siguienteEstado
            )
        }
    }

    fun cancelar(pedido: PedidoConDetalle) {
        val estadoActual = runCatching {
            EstadoPedido.valueOf(pedido.pedido.estado)
        }.getOrNull() ?: return

        if (
            estadoActual == EstadoPedido.ENTREGADO ||
            estadoActual == EstadoPedido.CANCELADO
        ) {
            return
        }

        viewModelScope.launch {
            repo.cambiarEstado(
                pedidoId = pedido.pedido.id,
                estado = EstadoPedido.CANCELADO
            )
        }
    }

    fun cambiarEstadoDetalle(
        pedidoId: Int,
        estado: EstadoPedido
    ) {
        viewModelScope.launch {
            repo.cambiarEstado(pedidoId, estado)
        }
    }

    fun cancelarPorId(pedidoId: Int) {
        viewModelScope.launch {
            val detalle = repo.observarPedido(pedidoId).first()
                ?: return@launch

            cancelar(detalle)
        }
    }
}
