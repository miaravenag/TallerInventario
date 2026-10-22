package com.example.tallerinventario.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tallerinventario.ui.login.LoginScreen
import com.example.tallerinventario.ui.login.LoginViewModel
import com.example.tallerinventario.ui.menu.MenuScreen
import com.example.tallerinventario.ui.producto.ListaProductosScreen
import com.example.tallerinventario.ui.producto.ProductoFormScreen
import com.example.tallerinventario.ui.producto.ProductoViewModel
import com.example.tallerinventario.ui.qr.QrScannerScreen
import com.example.tallerinventario.ui.qr.QrViewModel

object Rutas {
    const val LOGIN = "login"
    const val MENU = "menu"
    const val LISTA = "lista_productos"
    const val FORM = "producto_form"
    const val QR = "qr_scanner"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // ViewModels creados una sola vez y compartidos entre las pantallas
    val loginViewModel: LoginViewModel = viewModel()
    val productoViewModel: ProductoViewModel = viewModel()
    val qrViewModel: QrViewModel = viewModel()

    NavHost(navController = navController, startDestination = Rutas.LOGIN) {

        composable(Rutas.LOGIN) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginExitoso = {
                    navController.navigate(Rutas.MENU) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.MENU) {
            MenuScreen(
                viewModel = productoViewModel,
                onProductos = { navController.navigate(Rutas.LISTA) },
                onEscanearQr = {
                    qrViewModel.reiniciar()
                    navController.navigate(Rutas.QR)
                },
                onCerrarSesion = {
                    loginViewModel.reiniciar()
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.MENU) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.LISTA) {
            ListaProductosScreen(
                viewModel = productoViewModel,
                onAgregar = {
                    productoViewModel.iniciarFormulario()
                    navController.navigate(Rutas.FORM)
                },
                onEditar = { id ->
                    productoViewModel.iniciarFormulario(id = id)
                    navController.navigate(Rutas.FORM)
                },
                onEscanearQr = {
                    qrViewModel.reiniciar()
                    navController.navigate(Rutas.QR)
                },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.FORM) {
            ProductoFormScreen(
                viewModel = productoViewModel,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.QR) {
            QrScannerScreen(
                viewModel = qrViewModel,
                onVolver = { navController.popBackStack() },
                onEditar = { id ->
                    productoViewModel.iniciarFormulario(id = id)
                    navController.navigate(Rutas.FORM)
                },
                onRegistrar = { codigo ->
                    productoViewModel.iniciarFormulario(codigo = codigo)
                    navController.navigate(Rutas.FORM)
                }
            )
        }
    }
}
