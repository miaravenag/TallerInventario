package com.example.tallerinventario.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tallerinventario.ui.login.LoginViewModel
import com.example.tallerinventario.ui.producto.ProductoViewModel
import com.example.tallerinventario.ui.qr.QrViewModel

class AppViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ProductoViewModel::class.java) ->
            ProductoViewModel(container.productoRepository)
        modelClass.isAssignableFrom(QrViewModel::class.java) ->
            QrViewModel(container.productoRepository)
        modelClass.isAssignableFrom(LoginViewModel::class.java) ->
            LoginViewModel(container.authRepository)
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    } as T
}
