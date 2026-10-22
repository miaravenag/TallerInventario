package com.example.tallerinventario.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    // Flow: la lista se actualiza sola cada vez que cambia la tabla (reactividad)
    @Query("SELECT * FROM productos ORDER BY nombre COLLATE NOCASE ASC")
    fun observarTodos(): Flow<List<ProductoEntity>>

    @Query(
        """SELECT * FROM productos
           WHERE nombre LIKE '%' || :texto || '%' OR codigo LIKE '%' || :texto || '%'
           ORDER BY nombre COLLATE NOCASE ASC"""
    )
    fun buscar(texto: String): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun obtenerPorId(id: Long): ProductoEntity?

    @Query("SELECT * FROM productos WHERE codigo = :codigo LIMIT 1")
    suspend fun obtenerPorCodigo(codigo: String): ProductoEntity?

    @Insert
    suspend fun insertar(producto: ProductoEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodos(productos: List<ProductoEntity>)

    @Update
    suspend fun actualizar(producto: ProductoEntity)

    @Query("DELETE FROM productos WHERE id = :id")
    suspend fun eliminarPorId(id: Long)
}
