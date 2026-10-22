package com.example.tallerinventario.data.local

import com.example.tallerinventario.model.Producto

fun ProductoEntity.toModel() = Producto(
    id = id, codigo = codigo, nombre = nombre, categoria = categoria,
    cantidad = cantidad, stockMinimo = stockMinimo
)

fun Producto.toEntity() = ProductoEntity(
    id = id, codigo = codigo, nombre = nombre, categoria = categoria,
    cantidad = cantidad, stockMinimo = stockMinimo
)
