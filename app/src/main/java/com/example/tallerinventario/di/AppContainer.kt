package com.example.tallerinventario.di

import android.content.Context
import com.example.tallerinventario.data.local.AppDatabase
import com.example.tallerinventario.data.repository.AuthRepository
import com.example.tallerinventario.data.repository.ProductoRepository

/**
 * Único lugar donde se construyen Room y los repositorios.
 * Los ViewModels los RECIBEN por constructor (inyección de dependencias manual).
 */
class AppContainer(context: Context) {
    private val database = AppDatabase.getDatabase(context.applicationContext)

    val productoRepository = ProductoRepository(database.productoDao())
    val authRepository = AuthRepository()
}
