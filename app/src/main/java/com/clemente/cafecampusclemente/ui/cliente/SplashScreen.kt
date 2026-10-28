package com.clemente.cafecampusclemente.ui.cliente



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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CafeFondo = Color(0xFFFFF7ED)
private val CafeNaranja = Color(0xFFEF5B0C)
private val CafeNaranjaClaro = Color(0xFFFFB86B)
private val CafeTexto = Color(0xFF273444)

@Composable
fun SplashScreen(
    onComenzar: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CafeFondo
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1.1f))

            // Logo de CafeCampus
            Box(
                modifier = Modifier
                    .size(106.dp)
                    .background(
                        color = CafeNaranja,
                        shape = RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalCafe,
                    contentDescription = "Logo CafeCampus",
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "CafeCampus",
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold,
                color = CafeTexto
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Pide sin hacer cola",
                fontSize = 14.sp,
                color = CafeTexto.copy(alpha = 0.65f)
            )

            Spacer(modifier = Modifier.weight(0.9f))

            // Iconos ilustrativos de productos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconoProducto(
                    color = CafeNaranjaClaro,
                    icono = {
                        Icon(
                            imageVector = Icons.Default.LocalCafe,
                            contentDescription = "Café",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                )

                Spacer(modifier = Modifier.size(12.dp))

                IconoProducto(
                    color = Color(0xFFFFD9AE),
                    icono = {
                        Icon(
                            imageVector = Icons.Default.Restaurant,
                            contentDescription = "Comida",
                            tint = CafeNaranja,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                )

                Spacer(modifier = Modifier.size(12.dp))

                IconoProducto(
                    color = CafeNaranjaClaro,
                    icono = {
                        Icon(
                            imageVector = Icons.Default.LocalDrink,
                            contentDescription = "Bebida",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1.0f))

            // Botón principal
            Button(
                onClick = onComenzar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CafeNaranja,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Text(
                    text = "Comenzar",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun IconoProducto(
    color: Color,
    icono: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(82.dp)
            .background(
                color = color,
                shape = RoundedCornerShape(22.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        icono()
    }
}
