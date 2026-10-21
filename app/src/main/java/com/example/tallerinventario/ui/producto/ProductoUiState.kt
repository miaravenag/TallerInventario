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

/** Estado de la LISTA de productos. */
data class ProductoListaUiState(
    val productos: List<ProductoEntity> = emptyList(),
    val busqueda: String = "",
    val cargando: Boolean = true
)
