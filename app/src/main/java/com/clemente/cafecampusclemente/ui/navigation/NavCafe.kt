
package com.clemente.cafecampusclemente.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.clemente.cafecampusclemente.data.local.CafeDatabase
import com.clemente.cafecampusclemente.data.prefs.PreferenciasStore
import com.clemente.cafecampusclemente.data.repository.CafeRepositoryRoom
import com.clemente.cafecampusclemente.model.Rol
import com.clemente.cafecampusclemente.ui.cliente.DetalleProductoScreen
import com.clemente.cafecampusclemente.ui.cliente.LoginScreen
import com.clemente.cafecampusclemente.ui.cliente.MenuScreen
import com.clemente.cafecampusclemente.ViewModel.MenuViewModel
import com.clemente.cafecampusclemente.ViewModel.SesionViewModel

private object RutasCafe {
    const val LOGIN = "login"
    const val MENU = "menu"
    const val DETALLE = "detalle/{productoId}"
    const val PANEL = "panel"

    fun detalle(productoId: Int) = "detalle/$productoId"
}

@Composable
fun NavCafe() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val db = remember(context) {
        CafeDatabase.obtener(context.applicationContext)
    }

    val repository = remember(db) {
        CafeRepositoryRoom(
            productoDao = db.productoDao(),
            usuarioDao = db.usuarioDao(),
            pedidoDao = db.pedidoDao()
        )
    }

    val preferencias = remember(context) {
        PreferenciasStore(context.applicationContext)
    }

    val sesionViewModel: SesionViewModel = viewModel(
        factory = remember(repository, preferencias) {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>
                ): T {
                    if (modelClass.isAssignableFrom(
                            SesionViewModel::class.java
                        )
                    ) {
                        return SesionViewModel(
                            repository,
                            preferencias
                        ) as T
                    }

                    throw IllegalArgumentException(
                        "ViewModel desconocido: ${modelClass.name}"
                    )
                }
            }
        }
    )

    val menuViewModel: MenuViewModel = viewModel(
        factory = remember(repository) {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>
                ): T {
                    if (modelClass.isAssignableFrom(
                            MenuViewModel::class.java
                        )
                    ) {
                        return MenuViewModel(repository) as T
                    }

                    throw IllegalArgumentException(
                        "ViewModel desconocido: ${modelClass.name}"
                    )
                }
            }
        }
    )

    NavHost(
        navController = navController,
        startDestination = RutasCafe.LOGIN
    ) {
        composable(RutasCafe.LOGIN) {
            LoginScreen(
                vm = sesionViewModel,
                onLoginExitoso = { rol ->
                    val destino = if (rol == Rol.ADMIN) {
                        RutasCafe.PANEL
                    } else {
                        RutasCafe.MENU
                    }

                    navController.navigate(destino) {
                        popUpTo(RutasCafe.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(RutasCafe.MENU) {
            MenuScreen(
                vm = menuViewModel,
                onProducto = { productoId ->
                    navController.navigate(
                        RutasCafe.detalle(productoId)
                    )
                },
                onPedidos = {
                    // La pantalla de pedidos se implementará
                    // en un commit posterior.
                },
                onPerfil = {
                    // La pantalla de perfil se implementará
                    // en un commit posterior.
                }
            )
        }

        composable(
            route = RutasCafe.DETALLE,
            arguments = listOf(
                navArgument("productoId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val productoId =
                backStackEntry.arguments?.getInt("productoId")

            val productos by menuViewModel.productos.collectAsState()

            val producto = productos.firstOrNull {
                it.id == productoId
            }

            if (producto != null) {
                DetalleProductoScreen(
                    producto = producto,
                    vm = menuViewModel,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onAgregado = {
                        navController.popBackStack()
                    }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Producto no encontrado")
                }
            }
        }

        composable(RutasCafe.PANEL) {
            PantallaTemporal(
                texto = "Panel del administrador"
            )
        }
    }
}

@Composable
private fun PantallaTemporal(texto: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(texto)
    }
}

