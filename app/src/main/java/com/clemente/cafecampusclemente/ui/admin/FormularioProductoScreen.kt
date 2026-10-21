package com.clemente.cafecampusclemente.ui.admin



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Coffee
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clemente.cafecampusclemente.data.local.entity.Producto
import com.clemente.cafecampusclemente.ui.esImagenValida
import java.util.Locale

private val MoradoFormulario = Color(0xFF5046E5)
private val FondoIcono = Color(0xFFFFF0D6)

private val FondoFormulario: Color
    @Composable get() = MaterialTheme.colorScheme.background

private val TextoFormulario: Color
    @Composable get() = MaterialTheme.colorScheme.onSurface

private val TextoSecundarioFormulario: Color
    @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

private val FondoCampo: Color
    @Composable get() = MaterialTheme.colorScheme.surface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioProductoScreen(
    producto: Producto? = null,
    onGuardar: (Producto) -> Unit,
    onVolver: () -> Unit
) {
    var nombre by remember(producto?.id) {
        mutableStateOf(producto?.nombre.orEmpty())
    }
    var categoria by remember(producto?.id) {
        mutableStateOf(producto?.categoria.orEmpty())
    }
    var precio by remember(producto?.id) {
        mutableStateOf(producto?.precio?.toString().orEmpty())
    }
    var descripcion by remember(producto?.id) {
        mutableStateOf(producto?.descripcion.orEmpty())
    }
    var disponible by remember(producto?.id) {
        mutableStateOf(producto?.disponible ?: true)
    }
    var icono by remember(producto?.id) {
        mutableStateOf(producto?.icono ?: "🥪")
    }
    var error by remember {
        mutableStateOf<String?>(null)
    }
    var categoriaExpandida by remember {
        mutableStateOf(false)
    }
    var iconosExpandidos by remember {
        mutableStateOf(false)
    }

    val categorias = listOf(
        "Desayunos",
        "Almuerzos",
        "Bebidas",
        "Snacks"
    )

    val iconos = listOf("🥪", "🍛", "☕", "🍰", "🥤", "🍽️")

    Scaffold(
        containerColor = FondoFormulario,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (producto == null) {
                            "Nuevo producto"
                        } else {
                            "Editar producto"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoFormulario
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextoFormulario
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FondoFormulario
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 25.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Selector visual del icono del producto
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        iconosExpandidos = !iconosExpandidos
                    },
                shape = RoundedCornerShape(14.dp),
                color = FondoIcono
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 15.dp,
                        vertical = 12.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val imagenRes = producto?.imagenRes ?: 0
                    if (esImagenValida(imagenRes)) {
                        Image(
                            painter = painterResource(imagenRes),
                            contentDescription = producto?.nombre,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                    RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = icono,
                                fontSize = 27.sp
                            )
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Imagen del producto",
                            color = MoradoFormulario,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = "Toca para elegir icono",
                            color = TextoSecundarioFormulario,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            if (iconosExpandidos) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    iconos.forEach { opcion ->
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    icono = opcion
                                    iconosExpandidos = false
                                },
                            shape = RoundedCornerShape(10.dp),
                            color = if (icono == opcion) {
                                Color(0xFFE5E2FF)
                            } else {
                                MaterialTheme.colorScheme.surface
                            },
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (icono == opcion) {
                                    MoradoFormulario
                                } else {
                                    MaterialTheme.colorScheme.outlineVariant
                                }
                            )
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 9.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(opcion, fontSize = 21.sp)
                            }
                        }
                    }
                }
            }

            // Nombre
            CampoFormulario(
                etiqueta = "Nombre del producto"
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        error = null
                    },
                    placeholder = {
                        Text("Ej. Empanada de carne")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = coloresCampos()
                )
            }

            // Categoría desplegable
            CampoFormulario(etiqueta = "Categoría") {
                ExposedDropdownMenuBox(
                    expanded = categoriaExpandida,
                    onExpandedChange = {
                        categoriaExpandida = !categoriaExpandida
                    }
                ) {
                    OutlinedTextField(
                        value = categoria,
                        onValueChange = {
                            categoria = it
                            error = null
                        },
                        readOnly = true,
                        placeholder = {
                            Text("Selecciona una categoría")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = categoriaExpandida
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(
                                MenuAnchorType.PrimaryNotEditable
                            ),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = coloresCampos()
                    )

                    ExposedDropdownMenu(
                        expanded = categoriaExpandida,
                        onDismissRequest = {
                            categoriaExpandida = false
                        }
                    ) {
                        categorias.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    categoria = opcion
                                    categoriaExpandida = false
                                    error = null
                                }
                            )
                        }
                    }
                }
            }

            // Precio
            CampoFormulario(etiqueta = "Precio (S/)") {
                OutlinedTextField(
                    value = precio,
                    onValueChange = {
                        precio = it.replace(',', '.')
                        error = null
                    },
                    placeholder = { Text("0.00") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = coloresCampos()
                )
            }

            // Descripción
            CampoFormulario(etiqueta = "Descripción") {
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    placeholder = {
                        Text("Describe el producto y sus ingredientes")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 3,
                    shape = RoundedCornerShape(10.dp),
                    colors = coloresCampos()
                )
            }

            // Disponibilidad
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = FondoCampo
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 10.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Disponible en el menú",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextoFormulario
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = "Los estudiantes podrán pedirlo",
                            fontSize = 10.sp,
                            color = TextoSecundarioFormulario
                        )
                    }

                    Switch(
                        checked = disponible,
                        onCheckedChange = { disponible = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MoradoFormulario
                        )
                    )
                }
            }

            error?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(2.dp))

            // Guardar
            Button(
                onClick = {
                    val precioNumero = precio.toDoubleOrNull()

                    when {
                        nombre.isBlank() -> {
                            error = "El nombre es obligatorio"
                        }

                        categoria.isBlank() -> {
                            error = "Selecciona una categoría"
                        }

                        precioNumero == null || !precioNumero.isFinite() ||
                                precioNumero <= 0.0 -> {
                            error = "El precio debe ser mayor que cero"
                        }

                        else -> {
                            error = null
                            onGuardar(
                                Producto(
                                    id = producto?.id ?: 0,
                                    nombre = nombre.trim(),
                                    descripcion = descripcion.trim(),
                                    categoria = categoria.trim(),
                                    precio = precioNumero,
                                    disponible = disponible,
                                    icono = icono,
                                    imagenRes = producto?.imagenRes ?: 0
                                )
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MoradoFormulario
                )
            ) {
                Text(
                    text = "Guardar producto",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            // Cancelar
            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MoradoFormulario
                )
            ) {
                Text(
                    text = "Cancelar",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CampoFormulario(
    etiqueta: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = etiqueta,
            fontSize = 10.sp,
            color = TextoSecundarioFormulario,
            modifier = Modifier.padding(start = 5.dp)
        )

        contenido()
    }
}

@Composable
private fun coloresCampos() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = FondoCampo,
    unfocusedContainerColor = FondoCampo,
    focusedBorderColor = MoradoFormulario,
    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
    focusedTextColor = TextoFormulario,
    unfocusedTextColor = TextoFormulario,
    focusedPlaceholderColor = TextoSecundarioFormulario,
    unfocusedPlaceholderColor = TextoSecundarioFormulario
)
