package com.clemente.cafecampusclemente.ui.admin


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clemente.cafecampusclemente.ViewModel.PanelAdminViewModel
import java.text.NumberFormat
import java.util.Locale

private val MoradoCafe = Color(0xFF5046E5)
private val FondoCafe = Color(0xFFF7F7FC)
private val AzulSuave = Color(0xFFDCEAFF)
private val NaranjaSuave = Color(0xFFFFF0C2)
private val VerdeSuave = Color(0xFFD9FBE6)
private val TextoCafe = Color(0xFF20243A)
private val TextoSecundario = Color(0xFF777B91)
private val RojoSuave = Color(0xFFFFE2E2)

@Composable
fun PanelScreen(
    vm: PanelAdminViewModel,
    onVerPedidos: () -> Unit,
    onIrMenu: () -> Unit = {},
    onIrAjustes: () -> Unit = {}
) {
    val nuevos by vm.nuevos.collectAsState()
    val preparando by vm.preparando.collectAsState()
    val listos by vm.listos.collectAsState()
    val ventasHoy by vm.ventasHoy.collectAsState()

    Scaffold(
        containerColor = FondoCafe,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = MoradoCafe
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Default.BarChart,
                            contentDescription = "Panel"
                        )
                    },
                    label = { Text("Panel") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onVerPedidos,
                    icon = {
                        Icon(
                            Icons.Default.ShoppingBag,
                            contentDescription = "Pedidos"
                        )
                    },
                    label = { Text("Pedidos") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onIrMenu,
                    icon = {
                        Icon(
                            Icons.Default.Coffee,
                            contentDescription = "Menú"
                        )
                    },
                    label = { Text("Menú") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onIrAjustes,
                    icon = {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Ajustes"
                        )
                    },
                    label = { Text("Ajustes") }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                EncabezadoPanel()
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TarjetaEstado(
                        titulo = "Nuevos",
                        cantidad = nuevos,
                        fondo = AzulSuave,
                        texto = Color(0xFF2873D8),
                        modifier = Modifier.weight(1f)
                    )
                    TarjetaEstado(
                        titulo = "Preparando",
                        cantidad = preparando,
                        fondo = NaranjaSuave,
                        texto = Color(0xFFB87500),
                        modifier = Modifier.weight(1f)
                    )
                    TarjetaEstado(
                        titulo = "Listos",
                        cantidad = listos,
                        fondo = VerdeSuave,
                        texto = Color(0xFF16854B),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                TarjetaVentas(
                    ventas = ventasHoy,
                    pedidos = nuevos + preparando + listos
                )
            }

            item {
                GraficoPedidos()
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Nuevos pedidos",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoCafe
                    )
                    TextButton(onClick = onVerPedidos) {
                        Text(
                            text = "Ver todos",
                            color = MoradoCafe,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            item {
                if (nuevos > 0) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "$nuevos pedido(s) por preparar",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextoCafe
                                )
                                Spacer(Modifier.height(5.dp))
                                Text(
                                    text = "Revisa los pedidos recibidos y comienza su preparación.",
                                    fontSize = 12.sp,
                                    color = TextoSecundario
                                )
                            }

                            Spacer(Modifier.width(8.dp))

                            Button(
                                onClick = onVerPedidos,
                                shape = RoundedCornerShape(22.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MoradoCafe
                                )
                            ) {
                                Text(
                                    text = "Preparar",
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(22.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Coffee,
                                contentDescription = null,
                                tint = MoradoCafe,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Todo al día",
                                fontWeight = FontWeight.Bold,
                                color = TextoCafe
                            )
                            Text(
                                text = "No hay nuevos pedidos por preparar.",
                                color = TextoSecundario,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun EncabezadoPanel() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Panel de control",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextoCafe
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = "Cafetería · Hoy",
                fontSize = 13.sp,
                color = TextoSecundario
            )
        }

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notificaciones",
                tint = TextoCafe
            )
        }
    }
}

@Composable
private fun TarjetaEstado(
    titulo: String,
    cantidad: Int,
    fondo: Color,
    texto: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(86.dp),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = fondo),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = cantidad.toString(),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = texto
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = titulo,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = texto,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }
    }
}

@Composable
private fun TarjetaVentas(
    ventas: Double,
    pedidos: Int
) {
    val formatoMoneda = NumberFormat.getCurrencyInstance(
        Locale("es", "PE")
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = MoradoCafe),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(17.dp)
        ) {
            Text(
                text = "Ventas del día",
                color = Color(0xFFE1DEFF),
                fontSize = 13.sp
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatoMoneda.format(ventas),
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$pedidos pedidos",
                    color = Color.White,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun GraficoPedidos() {
    val horas = listOf("8h", "9h", "10h", "11h", "12h")
    val cantidades = listOf(3, 6, 9, 5, 7)
    val maximo = cantidades.maxOrNull() ?: 1

    Column {
        Text(
            text = "Pedidos por hora",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextoCafe
        )
        Spacer(Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(126.dp)
                    .padding(horizontal = 15.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                cantidades.forEachIndexed { indice, cantidad ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = cantidad.toString(),
                            fontSize = 10.sp,
                            color = TextoSecundario
                        )
                        Spacer(Modifier.height(4.dp))

                        Box(
                            modifier = Modifier
                                .width(25.dp)
                                .height((cantidad * 7 + 8).dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 6.dp,
                                        topEnd = 6.dp
                                    )
                                )
                                .background(
                                    if (cantidad == maximo) MoradoCafe
                                    else Color(0xFFB9BDF5)
                                )
                        )

                        Spacer(Modifier.height(5.dp))
                        Text(
                            text = horas[indice],
                            fontSize = 10.sp,
                            color = TextoSecundario
                        )
                    }
                }
            }
        }
    }
}
