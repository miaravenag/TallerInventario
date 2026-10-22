package com.example.tallerinventario.data.local

/** Productos FICTICIOS para pruebas y demostración (sin datos reales). */
object DatosEjemplo {
    val productos = listOf(
        ProductoEntity(codigo = "ACE-001", nombre = "Aceite motor 5W-30 1L", categoria = "Lubricantes", cantidad = 24, stockMinimo = 10),
        ProductoEntity(codigo = "ACE-002", nombre = "Aceite motor 10W-40 1L", categoria = "Lubricantes", cantidad = 8, stockMinimo = 10),
        ProductoEntity(codigo = "FIL-001", nombre = "Filtro de aire", categoria = "Filtros", cantidad = 12, stockMinimo = 5),
        ProductoEntity(codigo = "FIL-002", nombre = "Filtro de aceite", categoria = "Filtros", cantidad = 4, stockMinimo = 6),
        ProductoEntity(codigo = "FIL-003", nombre = "Filtro de combustible", categoria = "Filtros", cantidad = 0, stockMinimo = 3),
        ProductoEntity(codigo = "LIQ-001", nombre = "Líquido de frenos DOT4", categoria = "Líquidos", cantidad = 15, stockMinimo = 6),
        ProductoEntity(codigo = "LIQ-002", nombre = "Refrigerante 50/50 1L", categoria = "Líquidos", cantidad = 3, stockMinimo = 5),
        ProductoEntity(codigo = "BUJ-001", nombre = "Bujía de iridio", categoria = "Repuestos", cantidad = 20, stockMinimo = 8),
        ProductoEntity(codigo = "BAT-001", nombre = "Batería 12V 45Ah", categoria = "Repuestos", cantidad = 2, stockMinimo = 2),
        ProductoEntity(codigo = "ESC-001", nombre = "Escobillas limpiaparabrisas", categoria = "Repuestos", cantidad = 6, stockMinimo = 4),
        ProductoEntity(codigo = "ADI-001", nombre = "Aditivo limpia inyectores", categoria = "Aditivos", cantidad = 9, stockMinimo = 4),
        ProductoEntity(codigo = "LIM-001", nombre = "Desengrasante multiuso", categoria = "Limpieza", cantidad = 0, stockMinimo = 2)
    )
}
