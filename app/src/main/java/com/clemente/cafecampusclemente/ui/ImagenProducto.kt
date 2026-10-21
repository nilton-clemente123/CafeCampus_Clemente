package com.clemente.cafecampusclemente.ui

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Devuelve `true` cuando [imagenRes] corresponde a un drawable que Compose
 * puede pintar de forma segura con `painterResource()` (bitmap o vector).
 *
 * Los IDs de recursos de Android NO son estables entre compilaciones. Un ID
 * guardado en la base de datos puede quedar obsoleto tras reconstruir la app y
 * apuntar a un recurso distinto (por ejemplo, un `ColorDrawable`), lo que hace
 * que `painterResource()` lance un `ClassCastException` y cierre la app.
 */
@Composable
fun esImagenValida(imagenRes: Int): Boolean {
    if (imagenRes == 0) return false

    val context = LocalContext.current

    return remember(imagenRes) {
        try {
            val drawable = context.resources.getDrawable(imagenRes, context.theme)
            drawable is BitmapDrawable || drawable is VectorDrawable
        } catch (_: Exception) {
            false
        }
    }
}
