package com.clemente.cafecampusclemente.data.local.entity



import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "detalle_pedido",
    foreignKeys = [
        ForeignKey(
            entity = Pedido::class,
            parentColumns = ["id"],
            childColumns = ["pedidoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["pedidoId"])
    ]
)
data class DetallePedido(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val pedidoId: Int,

    val productoId: Int,

    val nombreProducto: String,

    val cantidad: Int,

    val precioUnitario: Double
)
