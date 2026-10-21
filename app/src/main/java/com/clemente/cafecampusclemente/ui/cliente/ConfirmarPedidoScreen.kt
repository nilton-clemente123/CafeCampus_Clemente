package com.clemente.cafecampusclemente.ui.cliente


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clemente.cafecampusclemente.ViewModel.PedidosClienteViewModel
import com.clemente.cafecampusclemente.model.ItemCarrito
import java.util.Locale

private val NaranjaCafe = Color(0xFFED5909)

private val FondoCafe: Color
    @Composable get() = MaterialTheme.colorScheme.background

private val TextoCafe: Color
    @Composable get() = MaterialTheme.colorScheme.onSurface

private val BordeCafe: Color
    @Composable get() = MaterialTheme.colorScheme.outlineVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarPedidoScreen(
    items: List<ItemCarrito>,
    vm: PedidosClienteViewModel,
    onVolver: () -> Unit,
    onSeguirPidiendo: () -> Unit,
    onPedidoConfirmado: (Int) -> Unit,
    onVaciarCarrito: () -> Unit
) {
    var horaRecojo by remember { mutableStateOf("10:30") }
    var pago by remember { mutableStateOf("CAJA") }
    var nota by remember { mutableStateOf("") }

    val cargando by vm.cargando.collectAsState()
    val error by vm.error.collectAsState()
    val pedidoCreadoId by vm.pedidoCreadoId.collectAsState()

    val subtotal = items.sumOf { it.producto.precio * it.cantidad }

    LaunchedEffect(pedidoCreadoId) {
        val id = pedidoCreadoId
        if (id != null) {
            onVaciarCarrito()
            vm.limpiarResultado()
            onPedidoConfirmado(id.toInt())
        }
    }

    Scaffold(
        containerColor = FondoCafe,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Confirmar pedido",
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
                    containerColor = FondoCafe
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FondoCafe)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        vm.confirmar(
                            items = items,
                            horaRecojo = horaRecojo,
                            pago = pago,
                            nota = nota
                        )
                    },
                    enabled = items.isNotEmpty() && !cargando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NaranjaCafe
                    )
                ) {
                    if (cargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Confirmar pedido", fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = onSeguirPidiendo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = NaranjaCafe
                    )
                ) {
                    Text("Seguir pidiendo", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 25.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Resumen del pedido",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextoCafe
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, BordeCafe)
            ) {
                if (items.isEmpty()) {
                    Text(
                        "Tu carrito está vacío.",
                        modifier = Modifier.padding(16.dp),
                        color = TextoCafe
                    )
                } else {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (item.producto.imagenRes != 0) {
                                    Image(
                                        painter = painterResource(item.producto.imagenRes),
                                        contentDescription = item.producto.nombre,
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .background(
                                                Color(0xFFFFEAD5),
                                                RoundedCornerShape(12.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            when (item.producto.nombre.lowercase()) {
                                                else -> "🍽️"
                                            },
                                            fontSize = 22.sp
                                        )
                                    }
                                }

                                Spacer(Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        item.producto.nombre,
                                        color = TextoCafe,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        "x${item.cantidad}",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 12.sp
                                    )
                                    if (item.nota.isNotBlank()) {
                                        Text(
                                            "Nota: ${item.nota}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Text(
                                    "S/ ${String.format(Locale.US, "%.2f", item.producto.precio * item.cantidad)}",
                                    color = TextoCafe,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            if (index < items.lastIndex) {
                                HorizontalDivider(color = BordeCafe)
                            }
                        }
                    }
                }
            }

            Text(
                "Hora de recojo",
                fontWeight = FontWeight.Bold,
                color = TextoCafe
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Ahora", "10:30", "11:00", "11:30").forEach { hora ->
                    val seleccion = horaRecojo == hora
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp),
                        onClick = {
                            horaRecojo = if (hora == "Ahora") "Ahora" else hora
                        },
                        shape = RoundedCornerShape(22.dp),
                        color = if (seleccion) NaranjaCafe else MaterialTheme.colorScheme.surface,
                        border = if (seleccion) null
                        else androidx.compose.foundation.BorderStroke(1.dp, BordeCafe)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                hora,
                                color = if (seleccion) Color.White else TextoCafe,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Text(
                "Forma de pago",
                fontWeight = FontWeight.Bold,
                color = TextoCafe
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("CAJA" to "Pagar en caja", "YAPE" to "Yape / Plin")
                    .forEach { (valor, etiqueta) ->
                        val seleccionado = pago == valor
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            onClick = { pago = valor },
                            shape = RoundedCornerShape(12.dp),
                            color = if (seleccionado) Color(0xFFFFEBD8)
                            else MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(
                                if (seleccionado) 1.5.dp else 1.dp,
                                if (seleccionado) NaranjaCafe else BordeCafe
                            )
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    etiqueta,
                                    color = if (seleccionado) NaranjaCafe else TextoCafe,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
            }

            OutlinedTextField(
                value = nota,
                onValueChange = { nota = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nota para el pedido") },
                placeholder = { Text("Ej. Entregar en la entrada") },
                minLines = 2,
                maxLines = 3,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NaranjaCafe,
                    focusedLabelColor = NaranjaCafe
                )
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, BordeCafe)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                        Text(
                            "S/ ${String.format(Locale.US, "%.2f", subtotal)}",
                            fontSize = 13.sp
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Servicio", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                        Text("S/ 0.00", fontSize = 13.sp)
                    }
                    HorizontalDivider(color = BordeCafe)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", fontWeight = FontWeight.Bold)
                        Text(
                            "S/ ${String.format(Locale.US, "%.2f", subtotal)}",
                            color = NaranjaCafe,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (error != null) {
                Text(
                    error.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            }
        }
    }
}

