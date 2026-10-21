package com.clemente.cafecampusclemente.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.clemente.cafecampusclemente.ViewModel.MenuAdminViewModel
import com.clemente.cafecampusclemente.data.local.entity.Producto
import java.util.Locale

private val MoradoMenu = Color(0xFF5046E5)
private val VerdeDisponible = Color(0xFFD9FBE6)
private val TextoDisponible = Color(0xFF16854B)
private val FondoAgotado = Color(0xFFFFE2E2)
private val TextoAgotado = Color(0xFFE54848)

private val FondoMenu: Color
    @Composable get() = MaterialTheme.colorScheme.background

private val TextoMenu: Color
    @Composable get() = MaterialTheme.colorScheme.onSurface

private val TextoSecundarioMenu: Color
    @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAdminScreen(
    vm: MenuAdminViewModel,
    onAgregar: () -> Unit,
    onEditar: (Producto) -> Unit,
    onVolver: () -> Unit = {}
) {
    val productos by vm.productos.collectAsStateWithLifecycle()
    var productoEliminar by remember { mutableStateOf<Producto?>(null) }
    var categoriaSeleccionada by remember { mutableStateOf("Todo") }

    val categorias = listOf("Todo", "Desayunos", "Almuerzos", "Bebidas")

    val productosFiltrados = productos.filter { producto ->
        categoriaSeleccionada == "Todo" ||
                producto.categoria.equals(categoriaSeleccionada, ignoreCase = true)
    }

    Scaffold(
        containerColor = FondoMenu,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Gestión del menú",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoMenu
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextoMenu
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FondoMenu
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregar,
                shape = RoundedCornerShape(17.dp),
                containerColor = MoradoMenu,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar producto"
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
            // Filtros por categoría
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                categorias.forEach { categoria ->
                    val seleccionada = categoriaSeleccionada == categoria

                    FilterChip(
                        selected = seleccionada,
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
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MoradoMenu,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = TextoSecundarioMenu
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = seleccionada,
                            borderColor = MaterialTheme.colorScheme.outlineVariant,
                            selectedBorderColor = MoradoMenu
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (productosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (productos.isEmpty()) {
                            "No hay productos registrados"
                        } else {
                            "No hay productos en esta categoría"
                        },
                        color = TextoSecundarioMenu,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        start = 14.dp,
                        end = 14.dp,
                        top = 8.dp,
                        bottom = 90.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    items(
                        items = productosFiltrados,
                        key = { it.id }
                    ) { producto ->
                        TarjetaProductoAdmin(
                            producto = producto,
                            onEditar = { onEditar(producto) },
                            onEliminar = {
                                productoEliminar = producto
                            },
                            onAlternarDisponibilidad = {
                                vm.alternarDisponibilidad(producto)
                            }
                        )
                    }
                }
            }
        }
    }

    productoEliminar?.let { producto ->
        AlertDialog(
            onDismissRequest = {
                productoEliminar = null
            },
            title = {
                Text("Eliminar producto")
            },
            text = {
                Text("¿Deseas eliminar ${producto.nombre}?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.eliminar(producto)
                        productoEliminar = null
                    }
                ) {
                    Text(
                        "Eliminar",
                        color = TextoAgotado
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        productoEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
private fun TarjetaProductoAdmin(
    producto: Producto,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
    onAlternarDisponibilidad: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono o ilustración del producto
            if (producto.imagenRes != 0) {
                Image(
                    painter = painterResource(producto.imagenRes),
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(11.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(Color(0xFFFFF0D6)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iconoProducto(producto),
                        fontSize = 25.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(9.dp))

            // Nombre, categoría, precio y disponibilidad
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = producto.nombre,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextoMenu,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${producto.categoria} · ${
                        String.format(
                            Locale("es", "PE"),
                            "S/ %.2f",
                            producto.precio
                        )
                    }",
                    fontSize = 10.sp,
                    color = TextoSecundarioMenu,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Surface(
                    onClick = onAlternarDisponibilidad,
                    shape = RoundedCornerShape(20.dp),
                    color = if (producto.disponible) {
                        VerdeDisponible
                    } else {
                        FondoAgotado
                    }
                ) {
                    Text(
                        text = if (producto.disponible) {
                            "Disponible"
                        } else {
                            "Agotado"
                        },
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 3.dp
                        ),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (producto.disponible) {
                            TextoDisponible
                        } else {
                            TextoAgotado
                        }
                    )
                }
            }

            // Editar
            IconButton(
                onClick = onEditar,
                modifier = Modifier.size(34.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar producto",
                    tint = MoradoMenu,
                    modifier = Modifier.size(17.dp)
                )
            }

            // Eliminar
            IconButton(
                onClick = onEliminar,
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar producto",
                    tint = TextoAgotado,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}

private fun iconoProducto(producto: Producto): String {
    val texto = "${producto.icono} ${producto.nombre} ${producto.categoria}"
        .lowercase()

    return when {
        "sandwich" in texto || "sándwich" in texto ||
                "pollo" in texto -> "🥪"

        "café" in texto || "cafe" in texto -> "☕"

        "keke" in texto || "queque" in texto ||
                "pastel" in texto || "torta" in texto -> "🍰"

        "menú del día" in texto || "menu del dia" in texto ||
                "almuerzo" in texto -> "🍛"

        "bebida" in texto || "jugo" in texto -> "🥤"

        else -> "🍽️"
    }
}