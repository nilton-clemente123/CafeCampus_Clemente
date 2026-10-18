package com.clemente.cafecampusclemente.ui.cliente

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clemente.cafecampusclemente.ViewModel.MenuViewModel
import com.clemente.cafecampusclemente.data.local.entity.Producto
import java.util.Locale

private val FondoCafe = Color(0xFFFFFBF7)
private val NaranjaCafe = Color(0xFFEA580C)
private val CremaCafe = Color(0xFFFFEDD5)


@Composable
fun MenuScreen(
    vm: MenuViewModel,
    onProducto: (Int) -> Unit,
    onPedidos: () -> Unit = {},
    onPerfil: () -> Unit = {}
) {
    var busqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember {
        mutableStateOf("Todo")
    }
    var destinoSeleccionado by remember {
        mutableIntStateOf(0)
    }

    val productos by vm.productos.collectAsState()
    val carrito by vm.carrito.collectAsState()

    val categorias = listOf(
        "Todo",
        "Desayunos",
        "Almuerzos",
        "Bebidas"
    )

    val productosFiltrados = productos.filter { producto ->
        val coincideBusqueda =
            producto.nombre.contains(busqueda, ignoreCase = true) ||
                    producto.descripcion.contains(busqueda, ignoreCase = true)

        val coincideCategoria = when (categoriaSeleccionada) {
            "Todo" -> true
            "Desayunos" -> producto.categoria.equals(
                "Desayunos", ignoreCase = true
            )
            "Almuerzos" -> producto.categoria.equals(
                "Almuerzos", ignoreCase = true
            )
            "Bebidas" -> producto.categoria.equals(
                "Bebidas", ignoreCase = true
            )
            else -> true
        }

        coincideBusqueda && coincideCategoria
    }

    Scaffold(
        containerColor = FondoCafe,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
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
                .padding(horizontal = 20.dp)
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
                        text = "¡Hola, Juan!",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF252936)
                    )

                    Spacer(Modifier.height(3.dp))

                    Text(
                        text = "¿Qué se te antoja hoy?",
                        fontSize = 12.sp,
                        color = Color(0xFF777B86)
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
                        onClick = onPedidos
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Ver carrito",
                            modifier = Modifier.size(25.dp),
                            tint = Color(0xFF252936)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Buscador.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color(0xFF8A8D96),
                    modifier = Modifier.size(19.dp)
                )

                Spacer(Modifier.width(8.dp))

                BasicTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF252936)
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (busqueda.isEmpty()) {
                                Text(
                                    text = "Buscar en el menú...",
                                    fontSize = 12.sp,
                                    color = Color(0xFF999CA5)
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
                            categoriaSeleccionada = categoria
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
                color = Color(0xFF252936)
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
                        color = Color(0xFF777B86)
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
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            // Imagen ilustrativa del producto.
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
                color = Color(0xFF30333B),
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
