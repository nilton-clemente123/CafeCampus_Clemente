

package com.clemente.cafecampusclemente.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.clemente.cafecampusclemente.data.local.entity.DetallePedido
import com.clemente.cafecampusclemente.data.local.entity.Pedido
import com.clemente.cafecampusclemente.data.local.entity.Usuario

data class PedidoConDetalle(

    @Embedded
    val pedido: Pedido,

    @Relation(
        parentColumn = "id",
        entityColumn = "pedidoId"
    )
    val detalles: List<DetallePedido>,

    @Relation(
        parentColumn = "clienteId",
        entityColumn = "id",
        entity = Usuario::class
    )
    val cliente: Usuario
)
