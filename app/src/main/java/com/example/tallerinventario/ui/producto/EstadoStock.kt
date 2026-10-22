package com.example.tallerinventario.ui.producto

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tallerinventario.model.EstadoStock

/** Icono + texto (no depende solo del color, como pide el caso en accesibilidad). */
@Composable
fun EstadoStockBadge(estado: EstadoStock) {
    val (icono, color) = when (estado) {
        EstadoStock.DISPONIBLE -> Icons.Default.CheckCircle to MaterialTheme.colorScheme.primary
        EstadoStock.STOCK_BAJO -> Icons.Default.Warning to MaterialTheme.colorScheme.tertiary
        EstadoStock.AGOTADO -> Icons.Default.Error to MaterialTheme.colorScheme.error
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(4.dp))
        Text(estado.etiqueta, color = color, style = MaterialTheme.typography.labelLarge)
    }
}
