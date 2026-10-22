package com.example.tallerinventario.data.repository

import com.example.tallerinventario.data.local.DatosEjemplo
import com.example.tallerinventario.data.local.ProductoDao
import com.example.tallerinventario.data.local.toEntity
import com.example.tallerinventario.data.local.toModel
import com.example.tallerinventario.model.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Única puerta de acceso a los datos de productos.
 * Convierte ProductoEntity (Room) <-> Producto (modelo): el resto de la app
 * nunca ve clases de Room.
 */
class ProductoRepository(private val dao: ProductoDao) {

    fun observarProductos(texto: String): Flow<List<Producto>> {
        val origen = if (texto.isBlank()) dao.observarTodos() else dao.buscar(texto.trim())
        return origen.map { lista -> lista.map { it.toModel() } }
    }

    suspend fun obtenerPorId(id: Long): Producto? = dao.obtenerPorId(id)?.toModel()

    suspend fun obtenerPorCodigo(codigo: String): Producto? = dao.obtenerPorCodigo(codigo)?.toModel()

    /** true si ya existe OTRO producto (distinto a idActual) con ese código. */
    suspend fun existeCodigo(codigo: String, idActual: Long): Boolean {
        val existente = dao.obtenerPorCodigo(codigo)
        return existente != null && existente.id != idActual
    }

    /** Inserta si es nuevo (id = 0) o actualiza si ya existe. */
    suspend fun guardar(producto: Producto) {
        val entidad = producto.toEntity()
        if (entidad.id == 0L) dao.insertar(entidad) else dao.actualizar(entidad)
    }

    suspend fun eliminar(id: Long) = dao.eliminarPorId(id)

    /** Inserta los productos ficticios (si el código ya existe, se ignora). */
    suspend fun cargarEjemplos() = dao.insertarTodos(DatosEjemplo.productos)
}
