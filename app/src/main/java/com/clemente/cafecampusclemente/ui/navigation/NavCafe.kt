
package com.clemente.cafecampusclemente.ui.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
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
import com.clemente.cafecampusclemente.ViewModel.MenuViewModel
import com.clemente.cafecampusclemente.ViewModel.PedidosClienteViewModel
import com.clemente.cafecampusclemente.ViewModel.SesionViewModel
import com.clemente.cafecampusclemente.data.local.CafeDatabase
import com.clemente.cafecampusclemente.data.prefs.PreferenciasStore
import com.clemente.cafecampusclemente.data.repository.CafeRepositoryRoom
import com.clemente.cafecampusclemente.model.Rol
import com.clemente.cafecampusclemente.ui.cliente.ConfirmarPedidoScreen
import com.clemente.cafecampusclemente.ui.cliente.DetalleProductoScreen
import com.clemente.cafecampusclemente.ui.cliente.LoginScreen
import com.clemente.cafecampusclemente.ui.cliente.MenuScreen
import com.clemente.cafecampusclemente.ui.cliente.MisPedidosScreen
import com.clemente.cafecampusclemente.ui.cliente.SeguimientoScreen

private object RutasCafe {
    const val LOGIN = "login"
    const val MENU = "menu"
    const val DETALLE = "detalle/{productoId}"
    const val CONFIRMAR_PEDIDO = "confirmarPedido"
    const val SEGUIMIENTO = "seguimiento/{pedidoId}"
    const val MIS_PEDIDOS = "misPedidos"
    const val PANEL = "panel"

    fun detalle(productoId: Int) = "detalle/$productoId"
    fun seguimiento(pedidoId: Int) = "seguimiento/$pedidoId"
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
                    if (
                        modelClass.isAssignableFrom(
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
                    if (
                        modelClass.isAssignableFrom(
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

    val usuarioActual by sesionViewModel.usuarioActual.collectAsState()

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
                onPedido = {
                    navController.navigate(
                        RutasCafe.CONFIRMAR_PEDIDO
                    )
                },
                onPedidos = {
                    navController.navigate(
                        RutasCafe.MIS_PEDIDOS
                    )
                },
                onPerfil = {
                    // Pendiente hasta implementar la pantalla de perfil.
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
                PantallaTemporal("Producto no encontrado")
            }
        }

        composable(RutasCafe.CONFIRMAR_PEDIDO) {
            val clienteId = usuarioActual?.id

            if (clienteId == null) {
                PantallaTemporal(
                    texto = "Inicia sesión para confirmar tu pedido."
                )
            } else {
                val pedidosViewModel: PedidosClienteViewModel = viewModel(
                    key = "pedidos_cliente_$clienteId",
                    factory = remember(repository, clienteId) {
                        object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(
                                modelClass: Class<T>
                            ): T {
                                if (
                                    modelClass.isAssignableFrom(
                                        PedidosClienteViewModel::class.java
                                    )
                                ) {
                                    return PedidosClienteViewModel(
                                        repository = repository,
                                        clienteId = clienteId
                                    ) as T
                                }

                                throw IllegalArgumentException(
                                    "ViewModel desconocido: ${modelClass.name}"
                                )
                            }
                        }
                    }
                )

                val carrito by menuViewModel.carrito.collectAsState()

                ConfirmarPedidoScreen(
                    items = carrito,
                    vm = pedidosViewModel,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onSeguirPidiendo = {
                        navController.popBackStack(
                            RutasCafe.MENU,
                            inclusive = false
                        )
                    },
                    onPedidoConfirmado = { pedidoId ->
                        navController.navigate(
                            RutasCafe.seguimiento(pedidoId)
                        )
                    },
                    onVaciarCarrito = {
                        menuViewModel.vaciar()
                    }
                )
            }
        }

        composable(
            route = RutasCafe.SEGUIMIENTO,
            arguments = listOf(
                navArgument("pedidoId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val pedidoId =
                backStackEntry.arguments?.getInt("pedidoId")

            val clienteId = usuarioActual?.id

            if (pedidoId == null || clienteId == null) {
                PantallaTemporal(
                    texto = "No se pudo cargar el seguimiento."
                )
            } else {
                val pedidosViewModel: PedidosClienteViewModel = viewModel(
                    key = "pedidos_cliente_$clienteId",
                    factory = remember(repository, clienteId) {
                        object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(
                                modelClass: Class<T>
                            ): T {
                                if (
                                    modelClass.isAssignableFrom(
                                        PedidosClienteViewModel::class.java
                                    )
                                ) {
                                    return PedidosClienteViewModel(
                                        repository = repository,
                                        clienteId = clienteId
                                    ) as T
                                }

                                throw IllegalArgumentException(
                                    "ViewModel desconocido: ${modelClass.name}"
                                )
                            }
                        }
                    }
                )

                SeguimientoScreen(
                    pedidoId = pedidoId,
                    vm = pedidosViewModel,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onVerMisPedidos = {
                        navController.navigate(RutasCafe.MIS_PEDIDOS)
                    }
                )
            }
        }

        composable(RutasCafe.MIS_PEDIDOS) {
            val clienteId = usuarioActual?.id

            if (clienteId == null) {
                PantallaTemporal(
                    texto = "Inicia sesión para ver tus pedidos."
                )
            } else {
                val pedidosViewModel: PedidosClienteViewModel = viewModel(
                    key = "pedidos_cliente_$clienteId",
                    factory = remember(repository, clienteId) {
                        object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(
                                modelClass: Class<T>
                            ): T {
                                if (
                                    modelClass.isAssignableFrom(
                                        PedidosClienteViewModel::class.java
                                    )
                                ) {
                                    return PedidosClienteViewModel(
                                        repository = repository,
                                        clienteId = clienteId
                                    ) as T
                                }

                                throw IllegalArgumentException(
                                    "ViewModel desconocido: ${modelClass.name}"
                                )
                            }
                        }
                    }
                )

                MisPedidosScreen(
                    vm = pedidosViewModel,
                    onSeleccionarPedido = { pedidoId ->
                        navController.navigate(
                            RutasCafe.seguimiento(pedidoId)
                        )
                    }
                )
            }
        }

        composable(RutasCafe.PANEL) {
            PantallaTemporal("Panel del administrador")
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
