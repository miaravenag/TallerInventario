package com.example.tallerinventario.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla "productos". El código es único para evitar registros duplicados.
 */
@Entity(
    tableName = "productos",
    indices = [Index(value = ["codigo"], unique = true)]
)
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val codigo: String,
    val nombre: String,
    val categoria: String,
    val cantidad: Int,
    val stockMinimo: Int
)
