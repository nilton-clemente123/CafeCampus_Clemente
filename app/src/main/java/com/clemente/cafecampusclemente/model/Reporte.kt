package com.clemente.cafecampusclemente.model

// Resultado de agregación: producto más vendido del día.
data class ProductoMasVendido(
    val nombreProducto: String,
    val cantidad: Int
)

// Resultado de agregación: total de ventas por forma de pago.
data class VentasPorPago(
    val pago: String,
    val total: Double
)
