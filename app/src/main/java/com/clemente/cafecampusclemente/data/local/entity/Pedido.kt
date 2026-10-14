package com.clemente.cafecampusclemente.data.local.entity


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pedidos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"]
        )
    ],
    indices = [
        Index(value = ["clienteId"])
    ]
)
data class Pedido(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val clienteId: Int,

    // Guarda EstadoPedido.name
    val estado: String = "RECIBIDO",

    val horaRecojo: String,

    val pago: String,

    val nota: String,

    val total: Double,

    val fecha: Long = System.currentTimeMillis()
)
