package com.clemente.cafecampusclemente.ui.admin

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clemente.cafecampusclemente.data.local.relation.PedidoConDetalle
import com.clemente.cafecampusclemente.model.EstadoPedido
import java.util.Locale

private val AzulCafe = Color(0xFF2864E8)
private val MoradoCafe = Color(0xFF5145E5)
private val AmarilloNota = Color(0xFFFFF3C4)
private val TextoNota = Color(0xFF8C6817)

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
                title = {
                    Text(
                        text = "Detalle del pedido",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text(
                            text = "‹",
                            fontSize = 32.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 25.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cabecera del pedido
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pedido #${pedido.pedido.id.toString().padStart(4, '0')}",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        EstadoEtiqueta(estado = estadoActual)
                    }

                    Text(
                        text = "Hoy ${pedido.pedido.horaRecojo.let { "" }}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )

                    Text(
                        text = "Recojo: ${pedido.pedido.horaRecojo}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            }

            // Cliente, productos y total
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Cliente",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "${pedido.cliente.nombre} · Estudiante",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                    )

                    pedido.detalles.forEach { detalle ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = detalle.nombreProducto,
                                modifier = Modifier.weight(1f),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = "x${detalle.cantidad}",
                                modifier = Modifier.width(38.dp),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = moneda(
                                    detalle.cantidad * detalle.precioUnitario
                                ),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )

                        Text(
                            text = moneda(pedido.pedido.total),
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MoradoCafe
                        )
                    }
                }
            }

            // Nota del cliente
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AmarilloNota
                )
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "Nota del cliente",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoNota
                    )

                    Text(
                        text = pedido.pedido.nota.ifBlank {
                            "Sin indicaciones adicionales"
                        },
                        fontSize = 13.sp,
                        color = TextoNota
                    )
                }
            }

            // Selector de estado
            Text(
                text = "Cambiar estado",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(
                    EstadoPedido.RECIBIDO,
                    EstadoPedido.PREPARANDO,
                    EstadoPedido.LISTO,
                    EstadoPedido.ENTREGADO
                ).forEach { estado ->
                    FilterChip(
                        selected = estado == estadoActual,
                        onClick = {
                            if (
                                estado != estadoActual &&
                                estadoActual != EstadoPedido.CANCELADO &&
                                estadoActual != EstadoPedido.ENTREGADO
                            ) {
                                onCambiarEstado(estado)
                            }
                        },
                        label = {
                            Text(
                                text = nombreEstado(estado),
                                fontSize = 11.sp
                            )
                        },
                        enabled = estadoActual != EstadoPedido.CANCELADO &&
                                estadoActual != EstadoPedido.ENTREGADO
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Acción principal
            if (siguienteEstado != null) {
                Button(
                    onClick = {
                        onCambiarEstado(siguienteEstado)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MoradoCafe
                    )
                ) {
                    Text(
                        text = "Pasar a ${nombreEstado(siguienteEstado)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Cancelación del pedido
            if (
                estadoActual != EstadoPedido.ENTREGADO &&
                estadoActual != EstadoPedido.CANCELADO
            ) {
                OutlinedButton(
                    onClick = onCancelar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text = "Cancelar pedido",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun EstadoEtiqueta(estado: EstadoPedido) {
    val fondo = when (estado) {
        EstadoPedido.RECIBIDO -> Color(0xFFDCEAFF)
        EstadoPedido.PREPARANDO -> Color(0xFFFFE8BD)
        EstadoPedido.LISTO -> Color(0xFFDDF5E4)
        EstadoPedido.ENTREGADO -> Color(0xFFE5E0FF)
        EstadoPedido.CANCELADO -> Color(0xFFFFE0E0)
    }

    val texto = when (estado) {
        EstadoPedido.RECIBIDO -> Color(0xFF2864E8)
        EstadoPedido.PREPARANDO -> Color(0xFF986000)
        EstadoPedido.LISTO -> Color(0xFF187442)
        EstadoPedido.ENTREGADO -> Color(0xFF5145E5)
        EstadoPedido.CANCELADO -> Color(0xFFC62828)
    }

    Card(
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(containerColor = fondo)
    ) {
        Text(
            text = nombreEstado(estado),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = texto,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun nombreEstado(estado: EstadoPedido): String {
    return when (estado) {
        EstadoPedido.RECIBIDO -> "Recibido"
        EstadoPedido.PREPARANDO -> "Preparando"
        EstadoPedido.LISTO -> "Listo"
        EstadoPedido.ENTREGADO -> "Entregado"
        EstadoPedido.CANCELADO -> "Cancelado"
    }
}

private fun moneda(valor: Double): String {
    return String.format(Locale("es", "PE"), "S/ %.2f", valor)
}
