package com.clemente.cafecampusclemente.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clemente.cafecampusclemente.ViewModel.PedidosClienteViewModel

private val NaranjaSeguimiento = Color(0xFFED5909)

private val FondoSeguimiento: Color
    @Composable get() = MaterialTheme.colorScheme.background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoScreen(
    pedidoId: Int,
    vm: PedidosClienteViewModel,
    onVolver: () -> Unit,
    onVerMisPedidos: () -> Unit
) {
    val pedidoConDetalle by vm.pedido.collectAsState()

    LaunchedEffect(pedidoId) {
        vm.observarPedido(pedidoId)
    }

    val pedido = pedidoConDetalle?.pedido
    val estado = pedido?.estado ?: "RECIBIDO"

    val estados = listOf("RECIBIDO", "PREPARANDO", "LISTO", "ENTREGADO")
    val posicion = estados.indexOf(estado)

    Scaffold(
        containerColor = FondoSeguimiento,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Seguimiento",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FondoSeguimiento
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = false,
                    onClick = onVolver,
                    icon = { Text("⌂", fontSize = 22.sp) },
                    label = { Text("Menú") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = onVerMisPedidos,
                    icon = { Text("☰", fontSize = 20.sp) },
                    label = { Text("Pedidos") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NaranjaSeguimiento,
                        selectedTextColor = NaranjaSeguimiento
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onVerMisPedidos,
                    icon = { Text("♙", fontSize = 20.sp) },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = NaranjaSeguimiento
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            "Pedido #${pedido?.id ?: pedidoId}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Recojo a las ${pedido?.horaRecojo ?: "Por confirmar"}",
                            color = Color.White,
                            fontSize = 13.sp
                        )
                        Text(
                            if (estado == "PREPARANDO") {
                                "Tiempo estimado: 10 min"
                            } else {
                                textoEstadoSeguimiento(estado)
                            },
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = "Hora de recojo",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            if (estado == "CANCELADO") {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFE4E4)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Este pedido fue cancelado.",
                        modifier = Modifier.padding(16.dp),
                        color = Color(0xFFB3261E),
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    estados.forEachIndexed { index, etapa ->
                        val completado = posicion >= index
                        val actual = posicion == index

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 68.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(32.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(25.dp)
                                        .background(
                                            color = when {
                                                completado -> Color(0xFF169B62)
                                                actual -> NaranjaSeguimiento
                                                else -> MaterialTheme.colorScheme.surface
                                            },
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        if (completado) "✓" else "",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (index < estados.lastIndex) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(43.dp)
                                            .background(
                                                if (posicion > index) {
                                                    NaranjaSeguimiento
                                                } else {
                                                    Color(0xFFE5E0DA)
                                                }
                                            )
                                    )
                                }
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    textoEstadoSeguimiento(etapa),
                                    fontWeight = if (actual) FontWeight.Bold
                                    else FontWeight.Medium,
                                    color = if (completado) MaterialTheme.colorScheme.onSurface
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 15.sp
                                )
                                Text(
                                    descripcionEstadoSeguimiento(etapa),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        "Resumen del pedido",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Text(
                        "Pedido #${pedido?.id ?: pedidoId}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            "S/ ${String.format(java.util.Locale.US, "%.2f", pedido?.total ?: 0.0)}",
                            color = NaranjaSeguimiento,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = onVerMisPedidos,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ver mis pedidos", color = NaranjaSeguimiento)
            }
        }
    }
}

private fun textoEstadoSeguimiento(estado: String): String =
    when (estado) {
        "RECIBIDO" -> "Recibido"
        "PREPARANDO" -> "Preparando"
        "LISTO" -> "Listo"
        "ENTREGADO" -> "Entregado"
        "CANCELADO" -> "Cancelado"
        else -> estado
    }

private fun descripcionEstadoSeguimiento(estado: String): String =
    when (estado) {
        "RECIBIDO" -> "Tu pedido llegó a la cocina."
        "PREPARANDO" -> "Están cocinando tu pedido."
        "LISTO" -> "Puedes pasar a recogerlo."
        "ENTREGADO" -> "¡Buen provecho!"
        else -> ""
    }
