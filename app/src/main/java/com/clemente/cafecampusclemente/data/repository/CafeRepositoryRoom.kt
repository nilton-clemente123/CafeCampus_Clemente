package com.clemente.cafecampusclemente.data.repository

import com.clemente.cafecampusclemente.data.local.dao.PedidoDao
import com.clemente.cafecampusclemente.data.local.dao.ProductoDao
import com.clemente.cafecampusclemente.data.local.dao.UsuarioDao
import com.clemente.cafecampusclemente.data.local.entity.DetallePedido
import com.clemente.cafecampusclemente.data.local.entity.Pedido
import com.clemente.cafecampusclemente.data.local.entity.Producto
import com.clemente.cafecampusclemente.data.local.entity.Usuario
import com.clemente.cafecampusclemente.data.local.relation.PedidoConDetalle
import com.clemente.cafecampusclemente.model.EstadoPedido
import com.clemente.cafecampusclemente.model.ItemCarrito
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class CafeRepositoryRoom(
    private val productoDao: ProductoDao,
    private val usuarioDao: UsuarioDao,
    private val pedidoDao: PedidoDao
) : CafeRepository {

    // Usuarios

    override suspend fun login(
        correo: String,
        clave: String
    ): Usuario? {
        return usuarioDao.login(correo, clave)
    }

    override suspend fun usuarioPorRol(rol: String): Usuario? {
        return usuarioDao.primeroPorRol(rol)
    }

    // Productos

    override fun observarProductos(): Flow<List<Producto>> {
        return productoDao.observarTodos()
    }

    override suspend fun guardarProducto(p: Producto) {
        if (p.id == 0) {
            productoDao.insertar(p)
        } else {
            productoDao.actualizar(p)
        }
    }

    override suspend fun eliminarProducto(p: Producto) {
        productoDao.eliminar(p)
    }

    override suspend fun cambiarDisponibilidad(
        id: Int,
        disponible: Boolean
    ) {
        productoDao.cambiarDisponibilidad(id, disponible)
    }

    // Pedidos

    override suspend fun crearPedido(
        clienteId: Int,
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String
    ): Long {
        val detalles = items.map { item ->
            DetallePedido(
                id = 0,
                pedidoId = 0,
                productoId = item.producto.id,
                nombreProducto = item.producto.nombre,
                cantidad = item.cantidad,
                precioUnitario = item.producto.precio
            )
        }

        val total = items.sumOf { item ->
            item.producto.precio * item.cantidad
        }

        val pedido = Pedido(
            clienteId = clienteId,
            estado = EstadoPedido.RECIBIDO.name,
            horaRecojo = horaRecojo,
            pago = pago,
            nota = nota,
            total = total
        )

        return pedidoDao.crearPedido(pedido, detalles)
    }

    override fun observarPedidosCliente(
        clienteId: Int
    ): Flow<List<PedidoConDetalle>> {
        return pedidoDao.observarPorCliente(clienteId)
    }

    override fun observarPedidosPorEstado(
        estado: EstadoPedido
    ): Flow<List<PedidoConDetalle>> {
        return pedidoDao.observarPorEstado(estado.name)
    }

    override fun observarPedido(
        id: Int
    ): Flow<PedidoConDetalle?> {
        return pedidoDao.observarUno(id)
    }

    override suspend fun cambiarEstado(
        pedidoId: Int,
        estado: EstadoPedido
    ) {
        pedidoDao.cambiarEstado(pedidoId, estado.name)
    }

    // Panel administrativo

    override fun observarVentasHoy(
        inicioDia: Long
    ): Flow<Double> {
        return pedidoDao.ventasDesde(inicioDia)
    }

    override fun observarCantidadPorEstado(
        estado: EstadoPedido
    ): Flow<Int> {
        return pedidoDao.cantidadPorEstado(estado.name)
    }
}