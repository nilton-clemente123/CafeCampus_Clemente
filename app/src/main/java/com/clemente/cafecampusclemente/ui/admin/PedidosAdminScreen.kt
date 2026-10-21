package com.clemente.cafecampusclemente.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clemente.cafecampusclemente.ViewModel.PedidosAdminViewModel
import com.clemente.cafecampusclemente.data.local.relation.PedidoConDetalle
import com.clemente.cafecampusclemente.model.EstadoPedido
import java.text.NumberFormat
import java.util.Locale

private val PedidosMorado = Color(0xFF5046E5)
private val PedidosRojo = Color(0xFFE54848)

private val PedidosFondo: Color
    @Composable get() = MaterialTheme.colorScheme.background

private val PedidosTexto: Color
    @Composable get() = MaterialTheme.colorScheme.onSurface

private val PedidosSecundario: Color
    @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

@Composable
fun PedidosAdminScreen(
    vm: PedidosAdminViewModel,
    onVolver: () -> Unit = {},
    onVerDetalle: (PedidoConDetalle) -> Unit = {}
) {
    val estadoSeleccionado by vm.estadoSeleccionado.collectAsState()
    val pedidos by vm.pedidos.collectAsState()

    Scaffold(
        containerColor = PedidosFondo
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 25.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolver) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = PedidosTexto
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pedidos",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = PedidosTexto
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                FiltroEstado(
                    texto = "Nuevos",
                    estado = EstadoPedido.RECIBIDO,
                    seleccionado = estadoSeleccionado,
                    onSeleccionar = vm::seleccionarEstado,
                    modifier = Modifier.weight(1f)
                )
                FiltroEstado(
                    texto = "Preparando",
                    estado = EstadoPedido.PREPARANDO,
                    seleccionado = estadoSeleccionado,
                    onSeleccionar = vm::seleccionarEstado,
                    modifier = Modifier.weight(1.25f)
                )
                FiltroEstado(
                    texto = "Listos",
                    estado = EstadoPedido.LISTO,
                    seleccionado = estadoSeleccionado,
                    onSeleccionar = vm::seleccionarEstado,
                    modifier = Modifier.weight(0.85f)
                )
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = when (estadoSeleccionado) {
                    EstadoPedido.RECIBIDO -> "Pedidos nuevos"
                    EstadoPedido.PREPARANDO -> "En preparación"
                    EstadoPedido.LISTO -> "Pedidos listos"
                    else -> "Pedidos"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = PedidosTexto
            )

            Spacer(Modifier.height(10.dp))

            if (pedidos.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = PedidosMorado
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "No hay pedidos en este estado",
                        color = PedidosTexto,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Los nuevos pedidos aparecerán aquí automáticamente.",
                        color = PedidosSecundario,
                        fontSize = 12.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = pedidos,
                        key = { it.pedido.id }
                    ) { pedido ->
                        TarjetaPedidoAdmin(
                            pedido = pedido,
                            onAvanzar = { vm.avanzar(pedido) },
                            onCancelar = { vm.cancelar(pedido) },
                            onVerDetalle = { onVerDetalle(pedido) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FiltroEstado(
    texto: String,
    estado: EstadoPedido,
    seleccionado: EstadoPedido,
    onSeleccionar: (EstadoPedido) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = seleccionado == estado,
        onClick = { onSeleccionar(estado) },
        label = {
            Text(
                text = texto,
                maxLines = 1,
                fontSize = 11.sp
            )
        },
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
            selectedContainerColor = PedidosMorado,
            selectedLabelColor = Color.White,
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = PedidosSecundario
        )
    )
}

@Composable
private fun TarjetaPedidoAdmin(
    pedido: PedidoConDetalle,
    onAvanzar: () -> Unit,
    onCancelar: () -> Unit,
    onVerDetalle: () -> Unit
) {
    val estado = runCatching {
        EstadoPedido.valueOf(pedido.pedido.estado)
    }.getOrDefault(EstadoPedido.RECIBIDO)

    val nombreCliente = pedido.cliente.nombre
    val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "PE"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = onVerDetalle
    ) {
        Column(
            modifier = Modifier.padding(13.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pedido #${pedido.pedido.id}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = PedidosTexto
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = nombreCliente,
                        fontSize = 12.sp,
                        color = PedidosSecundario
                    )
                }

                EstadoEtiqueta(estado)
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Recojo: ${pedido.pedido.horaRecojo}",
                fontSize = 11.sp,
                color = PedidosSecundario
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "Total: ${formatoMoneda.format(pedido.pedido.total)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = PedidosTexto
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onAvanzar,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PedidosMorado
                    ),
                    enabled = estado != EstadoPedido.ENTREGADO &&
                            estado != EstadoPedido.CANCELADO
                ) {
                    Text(
                        text = when (estado) {
                            EstadoPedido.RECIBIDO -> "Pasar a Preparando"
                            EstadoPedido.PREPARANDO -> "Pasar a Listo"
                            EstadoPedido.LISTO -> "Entregar"
                            else -> "Finalizado"
                        },
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                OutlinedButton(
                    onClick = onCancelar,
                    shape = RoundedCornerShape(22.dp),
                    enabled = estado != EstadoPedido.ENTREGADO &&
                            estado != EstadoPedido.CANCELADO,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PedidosRojo
                    )
                ) {
                    Text(
                        text = "Cancelar",
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun EstadoEtiqueta(estado: EstadoPedido) {
    val (texto, fondo, colorTexto) = when (estado) {
        EstadoPedido.RECIBIDO ->
            Triple("Recibido", Color(0xFFDCEAFF), Color(0xFF2873D8))

        EstadoPedido.PREPARANDO ->
            Triple("Preparando", Color(0xFFFFF0C2), Color(0xFFB87500))

        EstadoPedido.LISTO ->
            Triple("Listo", Color(0xFFD9FBE6), Color(0xFF16854B))

        EstadoPedido.ENTREGADO ->
            Triple("Entregado", Color(0xFFE8E8F0), PedidosTexto)

        EstadoPedido.CANCELADO ->
            Triple("Cancelado", Color(0xFFFFE2E2), PedidosRojo)
    }

    Text(
        text = texto,
        modifier = Modifier
            .background(fondo, RoundedCornerShape(18.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        color = colorTexto
    )
}
