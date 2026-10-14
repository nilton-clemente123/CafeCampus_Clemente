package com.clemente.cafecampusclemente.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.clemente.cafecampusclemente.data.local.entity.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos ORDER BY nombre")
    fun observarTodos(): Flow<List<Producto>>

    @Query("SELECT COUNT(*) FROM productos")
    suspend fun contar(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: Producto): Long

    @Update
    suspend fun actualizar(producto: Producto)

    @Delete
    suspend fun eliminar(producto: Producto)

    @Query(
        "UPDATE productos SET disponible = :disponible WHERE id = :id"
    )
    suspend fun cambiarDisponibilidad(
        id: Int,
        disponible: Boolean
    )
}
