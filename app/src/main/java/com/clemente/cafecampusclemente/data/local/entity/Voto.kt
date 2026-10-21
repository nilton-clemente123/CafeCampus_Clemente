package com.clemente.cafecampusclemente.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "votos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["estudianteId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Producto::class,
            parentColumns = ["id"],
            childColumns = ["productoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["estudianteId"]),
        Index(value = ["productoId"])
    ]
)
data class Voto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val estudianteId: Int,

    val productoId: Int,

    val fecha: Long = System.currentTimeMillis()
)
