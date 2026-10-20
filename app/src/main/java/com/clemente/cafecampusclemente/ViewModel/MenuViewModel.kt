package com.clemente.cafecampusclemente.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clemente.cafecampusclemente.data.local.entity.Producto
import com.clemente.cafecampusclemente.data.repository.CafeRepository
import com.clemente.cafecampusclemente.model.ItemCarrito
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    private val repo: CafeRepository
) : ViewModel() {

    // Productos observados desde Room a través del Repository.
    val productos: StateFlow<List<Producto>> =
        repo.observarProductos().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Estado de búsqueda.
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    // Categoría seleccionada.
    private val _categoriaSeleccionada = MutableStateFlow("Todo")
    val categoriaSeleccionada: StateFlow<String> =
        _categoriaSeleccionada.asStateFlow()

    // Combina productos + búsqueda + categoría de forma reactiva.
    val productosFiltrados: StateFlow<List<Producto>> =
        combine(
            productos,
            _busqueda,
            _categoriaSeleccionada
        ) { lista, busqueda, categoria ->
            lista.filter { producto ->
                val coincideBusqueda = producto.nombre.contains(
                    busqueda.trim(),
                    ignoreCase = true
                )

                val coincideCategoria = categoria == "Todo" ||
                        producto.categoria.equals(categoria, ignoreCase = true)

                coincideBusqueda && coincideCategoria
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun cambiarBusqueda(texto: String) {
        _busqueda.value = texto
    }

    fun cambiarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }

    // Contenido del carrito.
    private val _carrito =
        MutableStateFlow<List<ItemCarrito>>(emptyList())

    val carrito: StateFlow<List<ItemCarrito>> =
        _carrito.asStateFlow()

    // Agrega productos y acumula cantidades si ya existen.
    fun agregar(
        producto: Producto,
        cantidad: Int,
        nota: String
    ) {
        if (!producto.disponible || cantidad < 1) return

        val actual = _carrito.value.toMutableList()
        val indice = actual.indexOfFirst {
            it.producto.id == producto.id
        }

        if (indice >= 0) {
            val anterior = actual[indice]

            actual[indice] = anterior.copy(
                cantidad = anterior.cantidad + cantidad,
                nota = nota.trim().ifBlank { anterior.nota }
            )
        } else {
            actual.add(
                ItemCarrito(
                    producto = producto,
                    cantidad = cantidad,
                    nota = nota.trim()
                )
            )
        }

        _carrito.value = actual
    }

    // Quita del carrito un producto por su ID.
    fun quitar(productoId: Int) {
        _carrito.value = _carrito.value.filter {
            it.producto.id != productoId
        }
    }

    // Vacía todo el carrito.
    fun vaciar() {
        _carrito.value = emptyList()
    }

    // Calcula el total actual del carrito.
    fun total(): Double {
        return _carrito.value.sumOf {
            it.producto.precio * it.cantidad
        }
    }
}
