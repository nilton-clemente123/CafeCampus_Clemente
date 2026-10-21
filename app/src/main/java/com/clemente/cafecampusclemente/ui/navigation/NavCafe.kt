
package com.clemente.cafecampusclemente.ui.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.clemente.cafecampusclemente.ViewModel.MenuAdminViewModel
import com.clemente.cafecampusclemente.ViewModel.MenuViewModel
import com.clemente.cafecampusclemente.ViewModel.PanelAdminViewModel
import com.clemente.cafecampusclemente.ViewModel.PedidosAdminViewModel
import com.clemente.cafecampusclemente.ViewModel.PedidosClienteViewModel
import com.clemente.cafecampusclemente.ViewModel.SesionViewModel
import com.clemente.cafecampusclemente.data.local.CafeDatabase
import com.clemente.cafecampusclemente.data.local.relation.PedidoConDetalle
import com.clemente.cafecampusclemente.data.prefs.PreferenciasStore
import com.clemente.cafecampusclemente.data.repository.CafeRepositoryRoom
import com.clemente.cafecampusclemente.model.Rol
import com.clemente.cafecampusclemente.ui.admin.AjustesScreen
import com.clemente.cafecampusclemente.ui.admin.BarraNavegacionAdmin
import com.clemente.cafecampusclemente.ui.admin.DetallePedidoAdminScreen
import com.clemente.cafecampusclemente.ui.admin.FormularioProductoScreen
import com.clemente.cafecampusclemente.ui.admin.MenuAdminScreen
import com.clemente.cafecampusclemente.ui.admin.PanelScreen
import com.clemente.cafecampusclemente.ui.admin.PedidosAdminScreen
import com.clemente.cafecampusclemente.ui.cliente.ConfirmarPedidoScreen
import com.clemente.cafecampusclemente.ui.cliente.DetalleProductoScreen
import com.clemente.cafecampusclemente.ui.cliente.LoginScreen
import com.clemente.cafecampusclemente.ui.cliente.MenuScreen
import com.clemente.cafecampusclemente.ui.cliente.MisPedidosScreen
import com.clemente.cafecampusclemente.ui.cliente.SeguimientoScreen
import com.clemente.cafecampusclemente.ui.cliente.SplashScreen

private object RutasCafe {
    const val LOGIN = "login"
    const val MENU = "menu"
    const val DETALLE = "detalle/{productoId}"
    const val CONFIRMAR_PEDIDO = "confirmarPedido"
    const val SEGUIMIENTO = "seguimiento/{pedidoId}"
    const val MIS_PEDIDOS = "misPedidos"
    const val PANEL = "panel"

    const val INICIO = "inicio"


    const val PEDIDOS_ADMIN = "pedidos_admin"

    const val DETALLE_PEDIDO_ADMIN = "detalle_pedido_admin/{pedidoId}"

    const val MENU_ADMIN = "menu_admin"
    const val FORMULARIO_PRODUCTO =
        "formulario_producto?productoId={productoId}"


    const val AJUSTES = "ajustes"



    fun detalle(productoId: Int) = "detalle/$productoId"

    fun detallePedidoAdmin(pedidoId: Int): String =
        "detalle_pedido_admin/$pedidoId"

    fun formularioProducto(productoId: Int? = null): String {
        return if (productoId == null) {
            "formulario_producto"
        } else {
            "formulario_producto?productoId=$productoId"
        }
    }
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

    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    val mostrarBarraAdmin = rutaActual in setOf(
        RutasCafe.PANEL,
        RutasCafe.PEDIDOS_ADMIN,
        RutasCafe.MENU_ADMIN,
        RutasCafe.AJUSTES
    )

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


    val panelAdminViewModel: PanelAdminViewModel = viewModel(
        factory = remember(repository) {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>
                ): T {
                    if (modelClass.isAssignableFrom(PanelAdminViewModel::class.java)) {
                        return PanelAdminViewModel(repository) as T
                    }
                    throw IllegalArgumentException(
                        "ViewModel desconocido: ${modelClass.name}"
                    )
                }
            }
        }
    )

    val pedidosAdminViewModel: PedidosAdminViewModel = viewModel(
        factory = remember(repository) {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>
                ): T {
                    if (modelClass.isAssignableFrom(PedidosAdminViewModel::class.java)) {
                        return PedidosAdminViewModel(repository) as T
                    }
                    throw IllegalArgumentException(
                        "ViewModel desconocido: ${modelClass.name}"
                    )
                }
            }
        }
    )

    val menuAdminViewModel: MenuAdminViewModel = viewModel(
        factory = remember(repository) {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>
                ): T {
                    if (
                        modelClass.isAssignableFrom(
                            MenuAdminViewModel::class.java
                        )
                    ) {
                        return MenuAdminViewModel(repository) as T
                    }

                    throw IllegalArgumentException(
                        "ViewModel desconocido: ${modelClass.name}"
                    )
                }
            }
        }
    )

    var pedidoSeleccionado by remember {
        mutableStateOf<PedidoConDetalle?>(null)
    }

    Scaffold(
        bottomBar = {
            if (
                usuarioActual?.rol == Rol.ADMIN.name && mostrarBarraAdmin
            ) {
                BarraNavegacionAdmin(
                    rutaActual = rutaActual,
                    onIrPanel = {
                        navController.navigate(RutasCafe.PANEL) {
                            launchSingleTop = true
                        }
                    },
                    onIrPedidos = {
                        navController.navigate(RutasCafe.PEDIDOS_ADMIN) {
                            launchSingleTop = true
                        }
                    },
                    onIrMenu = {
                        navController.navigate(RutasCafe.MENU_ADMIN) {
                            launchSingleTop = true
                        }
                    },
                    onIrAjustes = {
                        navController.navigate(RutasCafe.AJUSTES) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = RutasCafe.INICIO
        ) {

            composable(RutasCafe.INICIO) {
                SplashScreen(
                    onComenzar = {
                        navController.navigate(RutasCafe.LOGIN) {
                            popUpTo(RutasCafe.INICIO) { inclusive = true }
                        }
                    }
                )
            }
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
                PanelScreen(
                    vm = panelAdminViewModel,
                    onVerPedidos = {
                        navController.navigate(RutasCafe.PEDIDOS_ADMIN) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(RutasCafe.PEDIDOS_ADMIN) {
                PedidosAdminScreen(
                    vm = pedidosAdminViewModel,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onVerDetalle = { pedido ->
                        pedidoSeleccionado = pedido
                        navController.navigate(
                            RutasCafe.detallePedidoAdmin(
                                pedido.pedido.id
                            )
                        )
                    }
                )
            }

            composable(
                route = RutasCafe.DETALLE_PEDIDO_ADMIN,
                arguments = listOf(
                    navArgument("pedidoId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->

                val pedidoId = backStackEntry.arguments
                    ?.getInt("pedidoId")

                if (pedidoId == null) {
                    PantallaTemporal("Pedido no encontrado")
                } else {
                    val pedidoFlow = remember(pedidoId) {
                        repository.observarPedido(pedidoId)
                    }

                    val pedido by pedidoFlow.collectAsState(
                        initial = null
                    )

                    if (pedido != null) {
                        DetallePedidoAdminScreen(
                            pedido = pedido!!,
                            onVolver = {
                                navController.popBackStack()
                            },
                            onCambiarEstado = { estado ->
                                pedidosAdminViewModel.cambiarEstadoDetalle(
                                    pedidoId,
                                    estado
                                )
                            },
                            onCancelar = {
                                pedidosAdminViewModel.cancelarPorId(
                                    pedidoId
                                )
                            }
                        )
                    } else {
                        PantallaTemporal("Cargando pedido...")
                    }
                }
            }

            composable(RutasCafe.MENU_ADMIN) {
                MenuAdminScreen(
                    vm = menuAdminViewModel,
                    onAgregar = {
                        navController.navigate(
                            RutasCafe.FORMULARIO_PRODUCTO
                        )
                    },
                    onEditar = { producto ->
                        navController.navigate(
                            RutasCafe.formularioProducto(
                                producto.id
                            )
                        )
                    },
                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = RutasCafe.FORMULARIO_PRODUCTO,
                arguments = listOf(
                    navArgument("productoId") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) { backStackEntry ->

                val productoId = backStackEntry.arguments
                    ?.getInt("productoId") ?: -1

                val productos by menuAdminViewModel.productos.collectAsState()

                val producto = productos.firstOrNull {
                    it.id == productoId
                }

                FormularioProductoScreen(
                    producto = producto,
                    onGuardar = { productoGuardado ->
                        menuAdminViewModel.guardar(productoGuardado)
                        navController.popBackStack()
                    },
                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }

            composable(RutasCafe.AJUSTES) {
                AjustesScreen(
                    vm = sesionViewModel,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onCambiarPerfil = {
                        val rolObjetivo = sesionViewModel.cambiarPerfil()

                        if (rolObjetivo != null) {
                            val destino = if (rolObjetivo == Rol.ADMIN) {
                                RutasCafe.PANEL
                            } else {
                                RutasCafe.MENU
                            }

                            navController.navigate(destino) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    onCerrarSesion = {
                        sesionViewModel.cerrarSesion()

                        navController.navigate(RutasCafe.LOGIN) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
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
