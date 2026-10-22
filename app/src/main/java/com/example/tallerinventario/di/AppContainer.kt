package com.example.tallerinventario.di

import android.content.Context
import com.example.tallerinventario.data.local.AppDatabase
import com.example.tallerinventario.data.repository.AuthRepository
import com.example.tallerinventario.data.repository.ProductoRepository

class AppContainer(context: Context) {
    private val database = AppDatabase.getDatabase(context.applicationContext)

    val productoRepository = ProductoRepository(database.productoDao())
    val authRepository = AuthRepository()
}
