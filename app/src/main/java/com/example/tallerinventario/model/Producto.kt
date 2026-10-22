package com.example.tallerinventario.model

enum class EstadoStock(val etiqueta: String) {
    DISPONIBLE("Disponible"),
    STOCK_BAJO("Stock bajo"),
    AGOTADO("Agotado")
}

data class Producto(
    val id: Long = 0,
    val codigo: String,
    val nombre: String,
    val categoria: String,
    val cantidad: Int,
    val stockMinimo: Int
) {
    val estadoStock: EstadoStock
        get() = when {
            cantidad == 0 -> EstadoStock.AGOTADO
            cantidad <= stockMinimo -> EstadoStock.STOCK_BAJO
            else -> EstadoStock.DISPONIBLE
        }
}
