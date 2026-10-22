package com.example.tallerinventario.data.repository

import com.example.tallerinventario.data.local.DatosEjemplo
import com.example.tallerinventario.data.local.ProductoDao
import com.example.tallerinventario.data.local.ProductoEntity
import kotlinx.coroutines.flow.Flow

/**
 * Única puerta de acceso a los datos de productos.
 * El ViewModel NO conoce el DAO ni Room: solo habla con este repositorio.
 */
class ProductoRepository(private val dao: ProductoDao) {

    fun observarProductos(texto: String): Flow<List<ProductoEntity>> =
        if (texto.isBlank()) dao.observarTodos() else dao.buscar(texto.trim())

    suspend fun obtenerPorId(id: Long): ProductoEntity? = dao.obtenerPorId(id)

    suspend fun obtenerPorCodigo(codigo: String): ProductoEntity? = dao.obtenerPorCodigo(codigo)

    /** true si ya existe OTRO producto (distinto a idActual) con ese código. */
    suspend fun existeCodigo(codigo: String, idActual: Long): Boolean {
        val existente = dao.obtenerPorCodigo(codigo)
        return existente != null && existente.id != idActual
    }

    /** Inserta si es nuevo (id = 0) o actualiza si ya existe. */
    suspend fun guardar(producto: ProductoEntity) {
        if (producto.id == 0L) dao.insertar(producto) else dao.actualizar(producto)
    }

    suspend fun eliminar(id: Long) = dao.eliminarPorId(id)

    /** Inserta los productos ficticios (si el código ya existe, se ignora). */
    suspend fun cargarEjemplos() = dao.insertarTodos(DatosEjemplo.productos)
}
