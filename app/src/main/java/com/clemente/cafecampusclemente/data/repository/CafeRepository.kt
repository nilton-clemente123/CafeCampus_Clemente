package com.clemente.cafecampusclemente.data.repository


import com.clemente.cafecampusclemente.data.local.entity.Producto
import com.clemente.cafecampusclemente.data.local.entity.Usuario
import com.clemente.cafecampusclemente.data.local.entity.Voto
import com.clemente.cafecampusclemente.data.local.relation.PedidoConDetalle
import com.clemente.cafecampusclemente.model.EstadoPedido
import com.clemente.cafecampusclemente.model.ItemCarrito
import com.clemente.cafecampusclemente.model.ProductoMasVendido
import com.clemente.cafecampusclemente.model.ProductoVotos
import com.clemente.cafecampusclemente.model.VentasPorPago
import kotlinx.coroutines.flow.Flow

interface CafeRepository {

    // Usuarios
    suspend fun login(correo: String, clave: String): Usuario?
    suspend fun usuarioPorRol(rol: String): Usuario?
    suspend fun usuarioPorCorreo(correo: String): Usuario?
    suspend fun registrarUsuario(usuario: Usuario): Long

    // Productos
    fun observarProductos(): Flow<List<Producto>>
    suspend fun guardarProducto(p: Producto)
    suspend fun eliminarProducto(p: Producto)
    suspend fun cambiarDisponibilidad(
        id: Int,
        disponible: Boolean
    )

    // Pedidos
    suspend fun crearPedido(
        clienteId: Int,
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String
    ): Long

    fun observarPedidosCliente(
        clienteId: Int
    ): Flow<List<PedidoConDetalle>>

    fun observarPedidosPorEstado(
        estado: EstadoPedido
    ): Flow<List<PedidoConDetalle>>

    fun observarPedido(
        id: Int
    ): Flow<PedidoConDetalle?>

    suspend fun cambiarEstado(
        pedidoId: Int,
        estado: EstadoPedido
    )

    // Panel administrativo
    fun observarVentasHoy(
        inicioDia: Long
    ): Flow<Double>

    fun observarCantidadPorEstado(
        estado: EstadoPedido
    ): Flow<Int>

    // Reporte del día
    fun observarProductoMasVendido(
        inicioDia: Long
    ): Flow<ProductoMasVendido?>

    fun observarVentasPorPago(
        inicioDia: Long
    ): Flow<List<VentasPorPago>>

    // Votación del plato del día
    suspend fun registrarVoto(voto: Voto)

    suspend fun votosDelEstudiante(
        estudianteId: Int,
        inicioDia: Long
    ): Int

    fun observarVotosEstudiante(
        estudianteId: Int,
        inicioDia: Long
    ): Flow<Int>

    fun observarVotosPorProducto(
        inicioDia: Long
    ): Flow<List<ProductoVotos>>
}