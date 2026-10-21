package com.clemente.cafecampusclemente.data.local.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.clemente.cafecampusclemente.data.local.entity.DetallePedido
import com.clemente.cafecampusclemente.data.local.entity.Pedido
import com.clemente.cafecampusclemente.data.local.relation.PedidoConDetalle
import com.clemente.cafecampusclemente.model.ProductoMasVendido
import com.clemente.cafecampusclemente.model.VentasPorPago
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPedido(pedido: Pedido): Long


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarDetalles(
        detalles: List<DetallePedido>
    )


    @Query("UPDATE pedidos SET estado = :estado WHERE id = :id")
    suspend fun cambiarEstado(
        id: Int,
        estado: String
    )


    @Transaction
    suspend fun crearPedido(
        pedido: Pedido,
        detalles: List<DetallePedido>
    ): Long {
        val pedidoId = insertarPedido(pedido)

        val detallesConPedidoId = detalles.map { detalle ->
            detalle.copy(
                id = 0,
                pedidoId = pedidoId.toInt()
            )
        }

        insertarDetalles(detallesConPedidoId)

        return pedidoId
    }


    @Transaction
    @Query(
        """
        SELECT * FROM pedidos
        WHERE clienteId = :clienteId
        ORDER BY fecha DESC
        """
    )
    fun observarPorCliente(
        clienteId: Int
    ): Flow<List<PedidoConDetalle>>


    @Transaction
    @Query(
        """
        SELECT * FROM pedidos
        WHERE estado = :estado
        ORDER BY fecha DESC
        """
    )
    fun observarPorEstado(
        estado: String
    ): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query(
        """
        SELECT * FROM pedidos
        WHERE id = :id
        LIMIT 1
        """
    )
    fun observarUno(
        id: Int
    ): Flow<PedidoConDetalle?>


    @Query(
        """
        SELECT COALESCE(SUM(total), 0.0)
        FROM pedidos
        WHERE fecha >= :inicio
          AND estado != 'CANCELADO'
        """
    )
    fun ventasDesde(inicio: Long): Flow<Double>


    @Query(
        """
        SELECT COUNT(*) FROM pedidos
        WHERE estado = :estado
        """
    )
    fun cantidadPorEstado(estado: String): Flow<Int>

    @Query(
        """
        SELECT nombreProducto, SUM(cantidad) AS cantidad
        FROM detalle_pedido
        WHERE pedidoId IN (
            SELECT id FROM pedidos
            WHERE fecha >= :inicio AND estado != 'CANCELADO'
        )
        GROUP BY nombreProducto
        ORDER BY cantidad DESC
        LIMIT 1
        """
    )
    fun productoMasVendidoDesde(inicio: Long): Flow<ProductoMasVendido?>

    @Query(
        """
        SELECT pago, SUM(total) AS total
        FROM pedidos
        WHERE fecha >= :inicio AND estado != 'CANCELADO'
        GROUP BY pago
        """
    )
    fun ventasPorPagoDesde(inicio: Long): Flow<List<VentasPorPago>>
}
