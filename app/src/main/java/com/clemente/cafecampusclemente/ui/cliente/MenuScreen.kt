package com.clemente.cafecampusclemente.ui.cliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clemente.cafecampusclemente.ViewModel.MenuViewModel
import com.clemente.cafecampusclemente.data.local.entity.Producto
import com.clemente.cafecampusclemente.ui.esImagenValida
import java.util.Locale

private val NaranjaCafe = Color(0xFFEA580C)
private val CremaCafe = Color(0xFFFFEDD5)

private val FondoCafe: Color
    @Composable get() = MaterialTheme.colorScheme.background


@Composable
fun MenuScreen(
    vm: MenuViewModel,
    onProducto: (Int) -> Unit,
    onPedido: () -> Unit = {},
    onPedidos: () -> Unit = {},
    onPerfil: () -> Unit = {},
    onVotar: () -> Unit = {}
) {
    var destinoSeleccionado by remember {
        mutableIntStateOf(0)
    }

    val busqueda by vm.busqueda.collectAsState()
    val categoriaSeleccionada by vm.categoriaSeleccionada.collectAsState()
    val productosFiltrados by vm.productosFiltrados.collectAsState()
    val carrito by vm.carrito.collectAsState()

    val categorias = listOf(
        "Todo",
        "Desayunos",
        "Almuerzos",
        "Bebidas"
    )

    Scaffold(
        containerColor = FondoCafe,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                NavigationBarItem(
                    selected = destinoSeleccionado == 0,
                    onClick = {
                        destinoSeleccionado = 0
                    },
                    icon = {
                        Icon(
                            Icons.Default.Storefront,
                            contentDescription = "Menú"
                        )
                    },
                    label = { Text("Menú") }
                )

                NavigationBarItem(
                    selected = destinoSeleccionado == 1,
                    onClick = {
                        destinoSeleccionado = 1
                        onPedidos()
                    },
                    icon = {
                        Icon(
                            Icons.Default.ReceiptLong,
                            contentDescription = "Pedidos"
                        )
                    },
                    label = { Text("Pedidos") }
                )

                NavigationBarItem(
                    selected = destinoSeleccionado == 2,
                    onClick = {
                        destinoSeleccionado = 2
                        onPerfil()
                    },
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Perfil"
                        )
                    },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 25.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            // Encabezado y acceso al carrito.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "¡Hola!",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.height(3.dp))

                    Text(
                        text = "¿Qué se te antoja hoy?",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                BadgedBox(
                    badge = {
                        if (carrito.isNotEmpty()) {
                            Badge {
                                Text(
                                    carrito.sumOf { it.cantidad }.toString()
                                )
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = onPedido
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Ver carrito",
                            modifier = Modifier.size(25.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Votación del plato del día
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onVotar),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = NaranjaCafe
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Vota por el plato del día",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Elige tu favorito para mañana",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        text = "›",
                        color = Color.White,
                        fontSize = 26.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Buscador.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(19.dp)
                )

                Spacer(Modifier.width(8.dp))

                BasicTextField(
                    value = busqueda,
                    onValueChange = vm::cambiarBusqueda,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (busqueda.isEmpty()) {
                                Text(
                                    text = "Buscar en el menú...",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(Modifier.height(12.dp))

            // Filtros por categoría.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                categorias.forEach { categoria ->
                    FilterChip(
                        selected = categoriaSeleccionada == categoria,
                        onClick = {
                            vm.cambiarCategoria(categoria)
                        },
                        label = {
                            Text(
                                text = categoria,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Populares hoy",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(10.dp))

            if (productosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron productos",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = productosFiltrados,
                        key = { it.id }
                    ) { producto ->
                        ProductoMenuCard(
                            producto = producto,
                            onAbrir = {
                                onProducto(producto.id)
                            },
                            onAgregar = {
                                vm.agregar(
                                    producto = producto,
                                    cantidad = 1,
                                    nota = ""
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductoMenuCard(
    producto: Producto,
    onAbrir: () -> Unit,
    onAgregar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onAbrir),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            // Imagen ilustrativa del producto.
            if (esImagenValida(producto.imagenRes)) {
                Image(
                    painter = painterResource(producto.imagenRes),
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(78.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(78.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            when (producto.icono.lowercase()) {
                                "cafe", "café" -> Color(0xFFE8D5C0)
                                "jugo" -> Color(0xFFFFE2CB)
                                "pan" -> Color(0xFFFFE8A3)
                                "plato" -> Color(0xFFFFEDD5)
                                "dulce" -> Color(0xFFFFE0E0)
                                else -> Color(0xFFFFEDD5)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iconoProductoMenu(producto.icono),
                        fontSize = 42.sp
                    )
                }
            }

            Spacer(Modifier.height(7.dp))

            Text(
                text = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .clickable(onClick = onAbrir),
                fontSize = 11.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2
            )

            Spacer(Modifier.height(3.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "S/ ${formatoPrecioMenu(producto.precio)}",
                    modifier = Modifier.weight(1f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaranjaCafe
                )


                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(
                            if (producto.disponible) {
                                NaranjaCafe
                            } else {
                                Color(0xFFB9B9B9)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = onAgregar,
                        enabled = producto.disponible,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar ${producto.nombre}",
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }

            }
        }
    }
}

private fun iconoProductoMenu(icono: String): String {
    return when (icono.lowercase()) {
        "cafe", "café" -> "☕"
        "plato" -> "🍽️"
        "jugo" -> "🧃"
        "pan" -> "🥪"
        "botella" -> "🍶"
        "dulce" -> "🍰"
        else -> "☕"
    }
}

private fun formatoPrecioMenu(precio: Double): String {
    return String.format(Locale.US, "%.2f", precio)
}
