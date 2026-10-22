package com.example.tallerinventario.ui.producto

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tallerinventario.data.local.AppDatabase
import com.example.tallerinventario.data.local.ProductoEntity
import com.example.tallerinventario.data.repository.ProductoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductoViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = ProductoRepository(AppDatabase.getDatabase(app).productoDao())

    // ---------------------------- LISTA ----------------------------
    private val _busqueda = MutableStateFlow("")
    private val _filtro = MutableStateFlow(FiltroStock.TODOS)

    @OptIn(ExperimentalCoroutinesApi::class)
    val listaState: StateFlow<ProductoListaUiState> =
        combine(_busqueda, _filtro) { texto, filtro -> texto to filtro }
            .flatMapLatest { (texto, filtro) ->
                repository.observarProductos(texto).map { productos ->
                    ProductoListaUiState(
                        productos = productos.filter { filtro.acepta(it) },
                        busqueda = texto,
                        filtro = filtro,
                        cargando = false
                    )
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProductoListaUiState())

    /** Totales para el menú principal (se actualizan solos al cambiar la tabla). */
    val resumen: StateFlow<ResumenInventario> = repository.observarProductos("")
        .map { lista ->
            ResumenInventario(
                total = lista.size,
                stockBajo = lista.count { it.estadoStock() == EstadoStock.STOCK_BAJO },
                agotados = lista.count { it.estadoStock() == EstadoStock.AGOTADO }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ResumenInventario())

    fun onBusquedaChange(texto: String) {
        _busqueda.value = texto
    }

    fun onFiltroChange(filtro: FiltroStock) {
        _filtro.value = filtro
    }

    /** Carga productos ficticios para demostración. */
    fun cargarEjemplos() {
        viewModelScope.launch { repository.cargarEjemplos() }
    }

    // --------------------------- FORMULARIO ---------------------------
    private val _formState = MutableStateFlow(ProductoUiState())
    val formState: StateFlow<ProductoUiState> = _formState.asStateFlow()

    /** Prepara el formulario: id > 0 = editar ese producto; id = 0 = producto nuevo
     *  (opcionalmente con el código ya escrito, p. ej. leído desde un QR). */
    fun iniciarFormulario(id: Long = 0L, codigo: String = "") {
        if (id > 0) {
            _formState.value = ProductoUiState(id = id)
            viewModelScope.launch {
                repository.obtenerPorId(id)?.let { _formState.value = it.toUiState() }
            }
        } else {
            _formState.value = ProductoUiState(codigo = codigo.trim().uppercase())
        }
    }

    fun limpiarFormulario() {
        _formState.value = ProductoUiState()
    }

    fun onCodigoChange(valor: String) =
        _formState.update { it.copy(codigo = valor, codigoError = null, errorGeneral = null) }

    fun onNombreChange(valor: String) =
        _formState.update { it.copy(nombre = valor, nombreError = null, errorGeneral = null) }

    fun onCategoriaChange(valor: String) =
        _formState.update { it.copy(categoria = valor, categoriaError = null, errorGeneral = null) }

    fun onCantidadChange(valor: String) =
        _formState.update { it.copy(cantidad = valor, cantidadError = null, errorGeneral = null) }

    fun onStockMinimoChange(valor: String) =
        _formState.update { it.copy(stockMinimo = valor, stockMinimoError = null, errorGeneral = null) }

    /** Valida y guarda en Room. La UI solo observa guardadoExitosamente para volver atrás. */
    fun guardar() {
        if (_formState.value.guardando) return
        if (!validar()) return

        val s = _formState.value
        val codigo = s.codigo.trim().uppercase()
        _formState.update { it.copy(guardando = true, errorGeneral = null) }

        viewModelScope.launch {
            if (repository.existeCodigo(codigo, s.id)) {
                _formState.update {
                    it.copy(guardando = false, codigoError = "Ya existe un producto con este código")
                }
                return@launch
            }
            try {
                repository.guardar(
                    ProductoEntity(
                        id = s.id,
                        codigo = codigo,
                        nombre = s.nombre.trim(),
                        categoria = s.categoria.trim(),
                        cantidad = s.cantidad.trim().toInt(),
                        stockMinimo = s.stockMinimo.trim().toInt()
                    )
                )
                _formState.update { it.copy(guardando = false, guardadoExitosamente = true) }
            } catch (e: Exception) {
                _formState.update {
                    it.copy(guardando = false, errorGeneral = "No se pudo guardar el producto. Intenta nuevamente.")
                }
            }
        }
    }

    /** Elimina el producto en edición (la UI pide confirmación antes de llamar). */
    fun eliminar() {
        val id = _formState.value.id
        if (id == 0L) return
        viewModelScope.launch {
            repository.eliminar(id)
            _formState.update { it.copy(guardadoExitosamente = true) }
        }
    }

    // --------------------------- VALIDACIONES ---------------------------
    private fun validar(): Boolean {
        val s = _formState.value
        val codigoError = if (s.codigo.isBlank()) "El código es obligatorio" else null
        val nombreError = if (s.nombre.isBlank()) "El nombre es obligatorio" else null
        val categoriaError = if (s.categoria.isBlank()) "La categoría es obligatoria" else null
        val cantidadError = validarEnteroNoNegativo(s.cantidad, "La cantidad")
        val stockMinimoError = validarEnteroNoNegativo(s.stockMinimo, "La existencia mínima")

        _formState.update {
            it.copy(
                codigoError = codigoError,
                nombreError = nombreError,
                categoriaError = categoriaError,
                cantidadError = cantidadError,
                stockMinimoError = stockMinimoError
            )
        }
        return listOfNotNull(
            codigoError, nombreError, categoriaError, cantidadError, stockMinimoError
        ).isEmpty()
    }

    private fun validarEnteroNoNegativo(valor: String, campo: String): String? {
        val texto = valor.trim()
        if (texto.isEmpty()) return "$campo es obligatoria"
        val numero = texto.toIntOrNull() ?: return "$campo debe ser un número entero"
        if (numero < 0) return "$campo no puede ser negativa"
        return null
    }

    private fun ProductoEntity.toUiState() = ProductoUiState(
        id = id,
        codigo = codigo,
        nombre = nombre,
        categoria = categoria,
        cantidad = cantidad.toString(),
        stockMinimo = stockMinimo.toString()
    )
}
