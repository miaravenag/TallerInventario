package com.example.tallerinventario.ui.producto

import com.example.tallerinventario.data.local.ProductoEntity

/** Estado del FORMULARIO de producto (registrar / editar). Los números van como String
 *  porque es lo que escribe el usuario; el ViewModel los valida y convierte. */
data class ProductoUiState(
    val id: Long = 0,
    val codigo: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val cantidad: String = "",
    val stockMinimo: String = "",

    val codigoError: String? = null,
    val nombreError: String? = null,
    val categoriaError: String? = null,
    val cantidadError: String? = null,
    val stockMinimoError: String? = null,
    val errorGeneral: String? = null,

    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false
) {
    val esEdicion: Boolean get() = id != 0L
}

/** Filtros rápidos de la lista. */
enum class FiltroStock(val etiqueta: String) {
    TODOS("Todos"),
    STOCK_BAJO("Stock bajo"),
    AGOTADOS("Agotados");

    fun acepta(p: ProductoEntity): Boolean = when (this) {
        TODOS -> true
        STOCK_BAJO -> p.estadoStock() == EstadoStock.STOCK_BAJO
        AGOTADOS -> p.estadoStock() == EstadoStock.AGOTADO
    }
}

/** Estado de la LISTA de productos. */
data class ProductoListaUiState(
    val productos: List<ProductoEntity> = emptyList(),
    val busqueda: String = "",
    val filtro: FiltroStock = FiltroStock.TODOS,
    val cargando: Boolean = true
)

/** Totales mostrados en el menú. */
data class ResumenInventario(
    val total: Int = 0,
    val stockBajo: Int = 0,
    val agotados: Int = 0
)
