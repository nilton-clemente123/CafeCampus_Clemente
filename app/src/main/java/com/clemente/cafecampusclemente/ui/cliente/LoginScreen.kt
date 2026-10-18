
package com.clemente.cafecampusclemente.ui.cliente

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clemente.cafecampusclemente.ViewModel.SesionViewModel
import com.clemente.cafecampusclemente.model.Rol


private val NaranjaCafe = Color(0xFFF15A0A)
private val VioletaAdmin = Color(0xFF5145E5)
private val FondoSelector = Color(0xFFF1F2F5)
private val TextoSecundario = Color(0xFF777B86)

@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onLoginExitoso: (Rol) -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var rolSeleccionado by remember {
        mutableStateOf(Rol.CLIENTE)
    }

    val usuario by vm.usuarioActual.collectAsState()
    val error by vm.error.collectAsState()

    val colorObjetivo = if (rolSeleccionado == Rol.CLIENTE) {
        NaranjaCafe
    } else {
        VioletaAdmin
    }

    val colorPrincipal by animateColorAsState(
        targetValue = colorObjetivo,
        animationSpec = tween(durationMillis = 350),
        label = "colorRol"
    )

    LaunchedEffect(usuario) {
        usuario?.let {
            val rol = if (it.rol == Rol.ADMIN.name) {
                Rol.ADMIN
            } else {
                Rol.CLIENTE
            }
            onLoginExitoso(rol)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "Bienvenido",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF252936)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Ingresa para continuar",
                fontSize = 14.sp,
                color = TextoSecundario
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Selector de rol
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        FondoSelector,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SelectorRol(
                    texto = "Estudiante",
                    seleccionado = rolSeleccionado == Rol.CLIENTE,
                    color = NaranjaCafe,
                    modifier = Modifier.weight(1f)
                ) {
                    rolSeleccionado = Rol.CLIENTE
                    vm.cerrarError()
                }

                SelectorRol(
                    texto = "Administrador",
                    seleccionado = rolSeleccionado == Rol.ADMIN,
                    color = VioletaAdmin,
                    modifier = Modifier.weight(1f)
                ) {
                    rolSeleccionado = Rol.ADMIN
                    vm.cerrarError()
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Campo de correo
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    vm.cerrarError()
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                label = { Text("Correo", fontSize = 12.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Correo",
                        tint = colorPrincipal
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorPrincipal,
                    unfocusedBorderColor = Color(0xFFE7E8ED),
                    focusedLabelColor = colorPrincipal,
                    cursorColor = colorPrincipal
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo de contraseña
            OutlinedTextField(
                value = clave,
                onValueChange = {
                    clave = it
                    vm.cerrarError()
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                label = { Text("Contraseña", fontSize = 12.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Contraseña",
                        tint = colorPrincipal
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorPrincipal,
                    unfocusedBorderColor = Color(0xFFE7E8ED),
                    focusedLabelColor = colorPrincipal,
                    cursorColor = colorPrincipal
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = colorPrincipal,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        // Función pendiente de implementar.
                    }
                )
            }

            // Error de autenticación
            if (error != null) {
                Text(
                    text = error.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    vm.login(
                        correo = correo,
                        clave = clave,
                        rolSeleccionado = rolSeleccionado
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorPrincipal
                )
            ) {
                Text(
                    text = "Ingresar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Contenido inferior animado según el rol
            AnimatedContent(
                targetState = rolSeleccionado,
                transitionSpec = {
                    fadeIn(
                        animationSpec = tween(250)
                    ) togetherWith fadeOut(
                        animationSpec = tween(150)
                    )
                },
                label = "contenidoPorRol"
            ) { rol ->
                if (rol == Rol.CLIENTE) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿No tienes cuenta? ",
                            fontSize = 12.sp,
                            color = TextoSecundario
                        )

                        Text(
                            text = "Regístrate",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaranjaCafe,
                            modifier = Modifier.clickable {
                                // Registro pendiente de implementar.
                            }
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp)
                            .background(
                                Color(0xFFEFEEFF),
                                RoundedCornerShape(14.dp)
                            )
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "Modo práctica",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = VioletaAdmin
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Puedes cambiar de perfil desde Ajustes para probar ambos roles.",
                            fontSize = 11.sp,
                            color = Color(0xFF77749D),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectorRol(
    texto: String,
    seleccionado: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    alSeleccionar: () -> Unit
) {
    val colorFondo by animateColorAsState(
        targetValue = if (seleccionado) color else Color.Transparent,
        animationSpec = tween(300),
        label = "fondoSelector"
    )

    val colorTexto by animateColorAsState(
        targetValue = if (seleccionado) Color.White
        else Color(0xFF666B76),
        animationSpec = tween(250),
        label = "textoSelector"
    )

    Surface(
        modifier = modifier
            .height(42.dp)
            .clickable(onClick = alSeleccionar),
        shape = RoundedCornerShape(12.dp),
        color = colorFondo
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = texto,
                fontSize = 12.sp,
                fontWeight = if (seleccionado) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                },
                color = colorTexto
            )
        }
    }
}
