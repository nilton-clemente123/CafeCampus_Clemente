package com.clemente.cafecampusclemente.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.clemente.cafecampusclemente.data.local.entity.Voto
import com.clemente.cafecampusclemente.model.ProductoVotos
import kotlinx.coroutines.flow.Flow

@Dao
interface VotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(voto: Voto): Long

    @Query(
        """
        SELECT COUNT(*) FROM votos
        WHERE estudianteId = :estudianteId AND fecha >= :inicio
        """
    )
    suspend fun contarVotosEstudianteAhora(
        estudianteId: Int,
        inicio: Long
    ): Int

    @Query(
        """
        SELECT COUNT(*) FROM votos
        WHERE estudianteId = :estudianteId AND fecha >= :inicio
        """
    )
    fun contarVotosEstudiante(
        estudianteId: Int,
        inicio: Long
    ): Flow<Int>

    @Query(
        """
        SELECT p.id AS productoId, p.nombre AS nombreProducto,
               COUNT(v.id) AS votos
        FROM votos v
        INNER JOIN productos p ON v.productoId = p.id
        WHERE v.fecha >= :inicio
        GROUP BY v.productoId
        ORDER BY votos DESC
        """
    )
    fun votosPorProductoDesde(inicio: Long): Flow<List<ProductoVotos>>
}
