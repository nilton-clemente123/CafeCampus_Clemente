package com.clemente.cafecampusclemente.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clemente.cafecampusclemente.data.local.relation.PedidoConDetalle
import com.clemente.cafecampusclemente.data.repository.CafeRepository
import com.clemente.cafecampusclemente.model.EstadoPedido
import com.clemente.cafecampusclemente.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PedidosClienteViewModel(
    private val repository: CafeRepository,
    private val clienteId: Int
) : ViewModel() {

    val misPedidos: StateFlow<List<PedidoConDetalle>> =
        repository.observarPedidosCliente(clienteId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val _pedido = MutableStateFlow<PedidoConDetalle?>(null)
    val pedido: StateFlow<PedidoConDetalle?> = _pedido.asStateFlow()

    private val _pedidoCreadoId = MutableStateFlow<Long?>(null)
    val pedidoCreadoId: StateFlow<Long?> = _pedidoCreadoId.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun observarPedido(id: Int) {
        repository.observarPedido(id).let { flujo ->
            viewModelScope.launch {
                flujo.collect { resultado ->
                    _pedido.value = resultado
                }
            }
        }
    }

    fun confirmar(
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String
    ) {
        if (items.isEmpty()) {
            _error.value = "El carrito está vacío."
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            try {
                val id = repository.crearPedido(
                    clienteId = clienteId,
                    items = items,
                    horaRecojo = horaRecojo,
                    pago = pago,
                    nota = nota
                )
                _pedidoCreadoId.value = id
            } catch (e: Exception) {
                _error.value = e.message
                    ?: "No se pudo confirmar el pedido."
            } finally {
                _cargando.value = false
            }
        }
    }

    fun limpiarResultado() {
        _pedidoCreadoId.value = null
        _error.value = null
    }

    fun limpiarError() {
        _error.value = null
    }

    fun activos(): List<PedidoConDetalle> =
        misPedidos.value.filter {
            it.pedido.estado in listOf(
                EstadoPedido.RECIBIDO.name,
                EstadoPedido.PREPARANDO.name,
                EstadoPedido.LISTO.name
            )
        }

    fun historial(): List<PedidoConDetalle> =
        misPedidos.value.filter {
            it.pedido.estado in listOf(
                EstadoPedido.ENTREGADO.name,
                EstadoPedido.CANCELADO.name
            )
        }
}
