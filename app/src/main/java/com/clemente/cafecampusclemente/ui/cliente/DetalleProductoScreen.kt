
package com.clemente.cafecampusclemente.ui.cliente

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.clemente.cafecampusclemente.ViewModel.MenuViewModel
import com.clemente.cafecampusclemente.data.local.entity.Producto
import java.util.Locale

private val NaranjaDetalle = Color(0xFFF15A0A)
private val CremaDetalle = Color(0xFFFFEDD5)
private val VerdeDisponible = Color(0xFFD9F8E7)

private val FondoDetalle: Color
    @Composable get() = MaterialTheme.colorScheme.background

private val TextoDetalle: Color
    @Composable get() = MaterialTheme.colorScheme.onSurface

private val GrisDetalle: Color
    @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    producto: Producto,
    vm: MenuViewModel,
    onVolver: () -> Unit,
    onAgregado: () -> Unit
) {
    var cantidad by remember(producto.id) {
        mutableIntStateOf(1)
    }
    var nota by remember(producto.id) {
        mutableStateOf("")
    }

    val disponible = producto.disponible
    val total = producto.precio * cantidad

    Scaffold(
        containerColor = FondoDetalle,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle del producto",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextoDetalle,



                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextoDetalle,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                color = FondoDetalle,
                shadowElevation = 0.dp
            ) {
                Button(
                    onClick = {
                        vm.agregar(producto, cantidad, nota)
                        onAgregado()
                    },
                    enabled = disponible,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 4.dp,
                            bottom = 12.dp
                        )
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NaranjaDetalle,
                        disabledContainerColor = Color.LightGray
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Text(
                        text = if (disponible) {
                            "Agregar al pedido"
                        } else {
                            "Producto agotado"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (producto.imagenRes != 0) {
                Image(
                    painter = painterResource(producto.imagenRes),
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(182.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(182.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CremaDetalle),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier.size(78.dp),
                        shape = CircleShape,
                        color = Color.White
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = iconoProducto(producto.icono),
                                fontSize = 42.sp
                            )
                        }
                    }
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = producto.nombre,
                    modifier = Modifier.weight(1f),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoDetalle
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "S/ ${formatoPrecio(producto.precio)}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaranjaDetalle
                )
            }

            // Disponibilidad y tiempo.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (disponible) {
                        VerdeDisponible
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    }
                ) {
                    Text(
                        text = if (disponible) "Disponible" else "Agotado",
                        modifier = Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 4.dp
                        ),
                        fontSize = 10.sp,
                        color = if (disponible) {
                            Color(0xFF127C44)
                        } else {
                            MaterialTheme.colorScheme.onErrorContainer
                        },
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Tiempo de preparación",
                    modifier = Modifier.size(13.dp),
                    tint = GrisDetalle
                )

                Text(
                    text = "10–15 min",
                    fontSize = 10.sp,
                    color = GrisDetalle
                )
            }

            // Descripción del producto.
            Text(
                text = producto.descripcion.ifBlank {
                    "Preparado al momento en nuestra cafetería."
                },
                fontSize = 10.sp,
                lineHeight = 15.sp,
                color = GrisDetalle
            )

            // Cantidad.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cantidad",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoDetalle
                )

                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = Color(0xFFF0EAE4)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 4.dp,
                            vertical = 3.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (cantidad > 1) cantidad--
                            },
                            enabled = disponible && cantidad > 1,
                            modifier = Modifier.size(29.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .background(
                                        CremaDetalle,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Reducir cantidad",
                                    tint = NaranjaDetalle,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Text(
                            text = cantidad.toString(),
                            modifier = Modifier.width(29.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoDetalle,
                            textAlign = TextAlign.Center
                        )

                        IconButton(
                            onClick = { cantidad++ },
                            enabled = disponible,
                            modifier = Modifier.size(29.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .background(
                                        NaranjaDetalle,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Aumentar cantidad",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Nota para la cocina.
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Nota para la cocina",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoDetalle
                )

                OutlinedTextField(
                    value = nota,
                    onValueChange = { nota = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(76.dp),
                    enabled = disponible,
                    placeholder = {
                        Text(
                            text = "Sin ají, por favor",
                            fontSize = 10.sp,
                            color = GrisDetalle
                        )
                    },
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = TextoDetalle
                    ),
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 3
                )
            }

            // Tarjeta del total.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(11.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 11.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Total",
                    fontSize = 9.sp,
                    color = GrisDetalle
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = "S/ ${formatoPrecio(total)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaranjaDetalle
                )
            }

            Spacer(Modifier.height(2.dp))
        }
    }
}

private fun iconoProducto(icono: String): String {
    return when (icono.lowercase()) {
        "cafe", "café" -> "☕"
        "plato" -> "🍽️"
        "jugo" -> "🧃"
        "pan" -> "🥪"
        "botella" -> "🍶"
        "dulce" -> "🍰"
        else -> "🍛"
    }
}

private fun formatoPrecio(precio: Double): String {
    return String.format(Locale.US, "%.2f", precio)
}
