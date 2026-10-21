package com.clemente.cafecampusclemente.model

// Resultado de la votación: cantidad de votos por producto.
data class ProductoVotos(
    val productoId: Int,
    val nombreProducto: String,
    val votos: Int
)
