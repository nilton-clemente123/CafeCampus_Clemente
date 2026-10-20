
package com.clemente.cafecampusclemente.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.clemente.cafecampusclemente.ViewModel.SesionViewModel

// Paleta clara
private val FondoAjustes = Color(0xFFF7F7FC)
private val MoradoAjustes = Color(0xFF5046E5)
private val TextoAjustes = Color(0xFF25283A)
private val SecundarioAjustes = Color(0xFF85899A)
private val BordeAjustes = Color(0xFFE9E9F1)
private val LavandaAjustes = Color(0xFFE5E9FF)
private val RojoAjustes = Color(0xFFE34B4B)
private val SuperficieAjustes = Color.White
private val InfoAjustes = Color(0xFFEEEEFF)
private val TrackInactivoAjustes = Color(0xFFD6D7E0)

// Paleta oscura
private val FondoAjustesOscuro = Color(0xFF12121A)
private val TextoAjustesOscuro = Color(0xFFECECF7)
private val SecundarioAjustesOscuro = Color(0xFF9C9EB6)
private val BordeAjustesOscuro = Color(0xFF2E2E3D)
private val LavandaAjustesOscuro = Color(0xFF2B2D52)
private val SuperficieAjustesOscuro = Color(0xFF1C1C27)
private val InfoAjustesOscuro = Color(0xFF262642)
private val TrackInactivoAjustesOscuro = Color(0xFF3C3D4E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(
    vm: SesionViewModel,
    onVolver: () -> Unit,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val modoOscuro by vm.modoOscuro.collectAsStateWithLifecycle()
    val recibirPedidos by vm.recibirPedidos.collectAsStateWithLifecycle()
    val usuario by vm.usuarioActual.collectAsStateWithLifecycle()

    val fondo = if (modoOscuro) FondoAjustesOscuro else FondoAjustes
    val texto = if (modoOscuro) TextoAjustesOscuro else TextoAjustes
    val secundario = if (modoOscuro) SecundarioAjustesOscuro else SecundarioAjustes
    val borde = if (modoOscuro) BordeAjustesOscuro else BordeAjustes
    val lavanda = if (modoOscuro) LavandaAjustesOscuro else LavandaAjustes
    val superficie = if (modoOscuro) SuperficieAjustesOscuro else SuperficieAjustes
    val info = if (modoOscuro) InfoAjustesOscuro else InfoAjustes
    val trackInactivo = if (modoOscuro) TrackInactivoAjustesOscuro else TrackInactivoAjustes

    Scaffold(
        containerColor = fondo,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ajustes",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = texto
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = texto
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = fondo
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 18.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjeta de perfil
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = superficie
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    borde
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                lavanda,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = MoradoAjustes,
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = when (usuario?.rol) {
                                "ADMIN" -> "Administrador"
                                "CLIENTE" -> "Estudiante"
                                else -> usuario?.rol ?: "Usuario"
                            },
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = texto
                        )

                        Text(
                            text = usuario?.correo ?: "Sin correo registrado",
                            fontSize = 12.sp,
                            color = secundario
                        )

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = lavanda
                        ) {
                            Text(
                                text = "Perfil activo",
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 3.dp
                                ),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MoradoAjustes
                            )
                        }
                    }
                }
            }

            // Botón destacado de cambio de perfil
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onCambiarPerfil),
                shape = RoundedCornerShape(17.dp),
                color = MoradoAjustes
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 18.dp,
                        vertical = 16.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )

                    Spacer(Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Cambiar de perfil",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(Modifier.height(3.dp))

                        Text(
                            text = "Modo práctico: ir a vista Estudiante",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.82f)
                        )
                    }

                    Text(
                        text = "›",
                        fontSize = 30.sp,
                        color = Color.White
                    )
                }
            }

            // Preferencias
            Text(
                text = "Preferencias",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = texto
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = superficie
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    borde
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp)
                ) {
                    FilaPreferencia(
                        titulo = "Modo oscuro",
                        descripcion = "Cambia el tema de la app",
                        icono = {
                            Icon(
                                imageVector = Icons.Default.DarkMode,
                                contentDescription = null,
                                tint = MoradoAjustes
                            )
                        },
                        checked = modoOscuro,
                        onCheckedChange = vm::cambiarModoOscuro,
                        colorTexto = texto,
                        colorSecundario = secundario,
                        colorTrackInactivo = trackInactivo
                    )

                    HorizontalDivider(color = borde)

                    FilaPreferencia(
                        titulo = "Notificaciones",
                        descripcion = "Avisar nuevos pedidos",
                        icono = {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = MoradoAjustes
                            )
                        },
                        checked = true,
                        onCheckedChange = {},
                        colorTexto = texto,
                        colorSecundario = secundario,
                        colorTrackInactivo = trackInactivo
                    )

                    HorizontalDivider(color = borde)

                    FilaPreferencia(
                        titulo = "Recibir pedidos",
                        descripcion = "Abrir o cerrar la recepción",
                        icono = {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = MoradoAjustes
                            )
                        },
                        checked = recibirPedidos,
                        onCheckedChange = vm::cambiarRecepcionPedidos,
                        colorTexto = texto,
                        colorSecundario = secundario,
                        colorTrackInactivo = trackInactivo
                    )
                }
            }

            // Información de persistencia
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = info
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 12.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "Se guardan en el dispositivo",
                        fontSize = 12.sp,
                        color = secundario
                    )
                    Text(
                        text = "con DataStore",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoradoAjustes
                    )
                }
            }

            Spacer(Modifier.weight(1f, fill = false))

            // Cerrar sesión
            OutlinedButton(
                onClick = onCerrarSesion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    RojoAjustes
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = RojoAjustes
                )
            ) {
                Text(
                    text = "Cerrar sesión",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun FilaPreferencia(
    titulo: String,
    descripcion: String,
    icono: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    colorTexto: Color,
    colorSecundario: Color,
    colorTrackInactivo: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 70.dp)
            .padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorTexto
            )

            Text(
                text = descripcion,
                fontSize = 11.sp,
                color = colorSecundario
            )
        }

        Spacer(Modifier.width(8.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MoradoAjustes,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = colorTrackInactivo,
                uncheckedBorderColor = colorTrackInactivo
            )
        )
    }
}
