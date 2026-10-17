package com.clemente.cafecampusclemente.model


import com.clemente.cafecampusclemente.data.local.entity.Producto

data class ItemCarrito(
    val producto: Producto,
    val cantidad: Int,
    val nota: String = ""
)