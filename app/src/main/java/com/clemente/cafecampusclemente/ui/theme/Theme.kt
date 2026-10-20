package com.clemente.cafecampusclemente.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MoradoPrincipal = Color(0xFF5046E5)
private val NaranjaPrincipal = Color(0xFFEA580C)

private val EsquemaClaro = lightColorScheme(
    primary = MoradoPrincipal,
    onPrimary = Color.White,
    secondary = NaranjaPrincipal,
    onSecondary = Color.White,
    background = Color(0xFFF7F7FC),
    onBackground = Color(0xFF20243A),
    surface = Color.White,
    onSurface = Color(0xFF20243A),
    surfaceVariant = Color(0xFFEFF0F7),
    onSurfaceVariant = Color(0xFF777B91),
    outline = Color(0xFFE9E9F1),
    outlineVariant = Color(0xFFE9E9F1),
    error = Color(0xFFE34B4B),
    onError = Color.White
)

private val EsquemaOscuro = darkColorScheme(
    primary = Color(0xFF8F86FF),
    onPrimary = Color.White,
    secondary = Color(0xFFF0852C),
    onSecondary = Color(0xFF3B1300),
    background = Color(0xFF12121A),
    onBackground = Color(0xFFECECF7),
    surface = Color(0xFF1C1C27),
    onSurface = Color(0xFFECECF7),
    surfaceVariant = Color(0xFF26263A),
    onSurfaceVariant = Color(0xFF9C9EB6),
    outline = Color(0xFF2E2E3D),
    outlineVariant = Color(0xFF2E2E3D),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410)
)

@Composable
fun CafecampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}