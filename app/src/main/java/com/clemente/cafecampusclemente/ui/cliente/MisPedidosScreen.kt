package com.clemente.cafecampusclemente.ui.cliente



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clemente.cafecampusclemente.ViewModel.PedidosClienteViewModel
import com.clemente.cafecampusclemente.data.local.relation.PedidoConDetalle
import java.util.Locale

private val NaranjaMisPedidos = Color(0xFFED5909)

private val FondoMisPedidos: Color
    @Composable get() = MaterialTheme.colorScheme.background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisPedidosScreen(
    vm: PedidosClienteViewModel,
    onSeleccionarPedido: (Int) -> Unit
) {
    val pedidos by vm.misPedidos.collectAsState()
    var mostrarActivos by remember { mutableStateOf(true) }

    val filtrados = pedidos.filter { item ->
        val estado = item.pedido.estado
        if (mostrarActivos) {
            estado in listOf("RECIBIDO", "PREPARANDO", "LISTO")
        } else {
            estado in listOf("ENTREGADO", "CANCELADO")
        }
    }

    Scaffold(
        containerColor = FondoMisPedidos,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mis pedidos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FondoMisPedidos
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Text("⌂", fontSize = 22.sp) },
                    label = { Text("Menú") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { mostrarActivos = true },
                    icon = { Text("☰", fontSize = 20.sp) },
                    label = { Text("Pedidos") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NaranjaMisPedidos,
                        selectedTextColor = NaranjaMisPedidos
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF1EAE2),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SelectorPedidos(
                    texto = "Activos",
                    seleccionado = mostrarActivos,
                    modifier = Modifier.weight(1f),
                    onClick = { mostrarActivos = true }
                )
                SelectorPedidos(
                    texto = "Historial",
                    seleccionado = !mostrarActivos,
                    modifier = Modifier.weight(1f),
                    onClick = { mostrarActivos = false }
                )
            }

            if (filtrados.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 42.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        if (mostrarActivos) "No tienes pedidos activos"
                        else "Tu historial está vacío",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF555555)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        if (mostrarActivos) {
                            "Cuando hagas un pedido, aparecerá aquí."
                        } else {
                            "Tus pedidos entregados y cancelados aparecerán aquí."
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    items(
                        items = filtrados,
                        key = { it.pedido.id }
                    ) { pedido ->
                        TarjetaPedidoCliente(
                            pedido = pedido,
                            onClick = {
                                onSeleccionarPedido(pedido.pedido.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectorPedidos(
    texto: String,
    seleccionado: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.height(38.dp),
        onClick = onClick,
        shape = RoundedCornerShape(13.dp),
        color = if (seleccionado) MaterialTheme.colorScheme.surface else Color.Transparent,
        shadowElevation = if (seleccionado) 1.dp else 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                texto,
                color = if (seleccionado) NaranjaMisPedidos else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun TarjetaPedidoCliente(
    pedido: PedidoConDetalle,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(13.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Pedido #${pedido.pedido.id}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Recojo: ${pedido.pedido.horaRecojo}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Ver seguimiento",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(15.dp)
                )
            }

            EstadoPedidoEtiqueta(pedido.pedido.estado)

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Total",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                Text(
                    "S/ ${String.format(Locale.US, "%.2f", pedido.pedido.total)}",
                    color = NaranjaMisPedidos,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun EstadoPedidoEtiqueta(estado: String) {
    val (etiqueta, fondo, texto) = when (estado) {
        "RECIBIDO" -> Triple(
            "Recibido",
            Color(0xFFE0F4E8),
            Color(0xFF187447)
        )
        "PREPARANDO" -> Triple(
            "Preparando",
            Color(0xFFFFF0C2),
            Color(0xFF946200)
        )
        "LISTO" -> Triple(
            "Listo",
            Color(0xFFD9F7E6),
            Color(0xFF187447)
        )
        "ENTREGADO" -> Triple(
            "Entregado",
            Color(0xFFE9E9ED),
            Color(0xFF555566)
        )
        "CANCELADO" -> Triple(
            "Cancelado",
            Color(0xFFFFE0E0),
            Color(0xFFB3261E)
        )
        else -> Triple(estado, Color(0xFFECECEC), Color.DarkGray)
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = fondo
    ) {
        Text(
            etiqueta,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            color = texto,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
