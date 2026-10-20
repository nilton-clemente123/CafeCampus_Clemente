package com.clemente.cafecampusclemente.ui.admin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BarraNavegacionAdmin(
    rutaActual: String?,
    onIrPanel: () -> Unit,
    onIrPedidos: () -> Unit,
    onIrMenu: () -> Unit,
    onIrAjustes: () -> Unit
) {
    val seleccionado = when (rutaActual) {
        "panel" -> "panel"
        "pedidos_admin" -> "pedidos"
        "menu_admin" -> "menu"
        "ajustes" -> "ajustes"
        else -> ""
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationBarItem(
            selected = seleccionado == "panel",
            onClick = onIrPanel,
            icon = {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = "Panel"
                )
            },
            label = { Text("Panel") }
        )

        NavigationBarItem(
            selected = seleccionado == "pedidos",
            onClick = onIrPedidos,
            icon = {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Pedidos"
                )
            },
            label = { Text("Pedidos") }
        )

        NavigationBarItem(
            selected = seleccionado == "menu",
            onClick = onIrMenu,
            icon = {
                Icon(
                    imageVector = Icons.Default.Coffee,
                    contentDescription = "Menú"
                )
            },
            label = { Text("Menú") }
        )

        NavigationBarItem(
            selected = seleccionado == "ajustes",
            onClick = onIrAjustes,
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Ajustes"
                )
            },
            label = { Text("Ajustes") }
        )
    }
}