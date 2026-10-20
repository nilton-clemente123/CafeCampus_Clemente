package com.clemente.cafecampusclemente.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clemente.cafecampusclemente.data.local.entity.Producto
import com.clemente.cafecampusclemente.data.repository.CafeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MenuAdminViewModel(
    private val repo: CafeRepository
) : ViewModel() {

    val productos = repo.observarProductos().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun guardar(producto: Producto) {
        viewModelScope.launch {
            repo.guardarProducto(producto)
        }
    }

    fun eliminar(producto: Producto) {
        viewModelScope.launch {
            repo.eliminarProducto(producto)
        }
    }

    fun alternarDisponibilidad(producto: Producto) {
        viewModelScope.launch {
            repo.cambiarDisponibilidad(
                id = producto.id,
                disponible = !producto.disponible
            )
        }
    }
}
