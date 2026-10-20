package com.clemente.cafecampusclemente.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.clemente.cafecampusclemente.data.local.relation.PedidoConDetalle
import com.clemente.cafecampusclemente.model.EstadoPedido

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePedidoAdminScreen(
    pedido: PedidoConDetalle,
    onVolver: () -> Unit,
    onCambiarEstado: (EstadoPedido) -> Unit,
    onCancelar: () -> Unit
) {
    val estadoActual = runCatching {
        EstadoPedido.valueOf(pedido.pedido.estado)
    }.getOrDefault(EstadoPedido.RECIBIDO)

    val siguienteEstado = when (estadoActual) {
        EstadoPedido.RECIBIDO -> EstadoPedido.PREPARANDO
        EstadoPedido.PREPARANDO -> EstadoPedido.LISTO
        EstadoPedido.LISTO -> EstadoPedido.ENTREGADO
        EstadoPedido.ENTREGADO,
        EstadoPedido.CANCELADO -> null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del pedido") },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Pedido #${pedido.pedido.id}",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text("Cliente: ${pedido.cliente.nombre}")
                        Text("Hora de recojo: ${pedido.pedido.horaRecojo}")
                        Text("Estado: ${estadoActual.name}")
                        Text(
                            "Total: S/ %.2f".format(pedido.pedido.total),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            item {
                Text(
                    "Productos del pedido",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(pedido.detalles) { detalle ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            detalle.nombreProducto,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text("Cantidad: ${detalle.cantidad}")
                        Text(
                            "Precio unitario: S/ %.2f"
                                .format(detalle.precioUnitario)
                        )
                        Text(
                            "Subtotal: S/ %.2f".format(
                                detalle.cantidad * detalle.precioUnitario
                            )
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Nota del cliente",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            pedido.pedido.nota.ifBlank {
                                "Sin indicaciones adicionales"
                            }
                        )
                    }
                }
            }

            item {
                Text(
                    "Cambiar estado",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                EstadoPedido.values().forEach { estado ->
                    if (estado != EstadoPedido.CANCELADO &&
                        estado != estadoActual
                    ) {
                        OutlinedButton(
                            onClick = { onCambiarEstado(estado) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        ) {
                            Text(estado.name)
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                if (siguienteEstado != null) {
                    Button(
                        onClick = {
                            onCambiarEstado(siguienteEstado)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Pasar a ${siguienteEstado.name}")
                    }
                }

                Spacer(Modifier.height(8.dp))

                if (estadoActual != EstadoPedido.ENTREGADO &&
                    estadoActual != EstadoPedido.CANCELADO
                ) {
                    OutlinedButton(
                        onClick = onCancelar,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Cancelar pedido")
                    }
                }
            }
        }
    }
}
