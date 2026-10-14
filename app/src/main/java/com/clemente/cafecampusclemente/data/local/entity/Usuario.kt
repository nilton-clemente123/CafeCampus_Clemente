package com.clemente.cafecampusclemente.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,

    val correo: String,

    val clave: String,

    // Guarda Rol.name: "CLIENTE" o "ADMIN"
    val rol: String
)
