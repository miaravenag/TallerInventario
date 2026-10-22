package com.example.tallerinventario.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tallerinventario.ui.producto.ProductoViewModel
import com.example.tallerinventario.ui.theme.AzulMarino
import com.example.tallerinventario.ui.theme.AzulOscuro

@Composable
fun MenuScreen(
    viewModel: ProductoViewModel,
    onProductos: () -> Unit,
    onEscanearQr: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val resumen by viewModel.resumen.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Encabezado con degradado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(AzulOscuro, AzulMarino)))
                .statusBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 28.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Hola, administrador", style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.8f))
                Text(
                    "Inventario del Taller",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text("Resumen de la bodega", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Tarjetas de resumen (se actualizan solas desde Room)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard(
                    valor = resumen.total, etiqueta = "Productos", icono = Icons.Default.Inventory2,
                    fondo = MaterialTheme.colorScheme.primaryContainer,
                    contenido = MaterialTheme.colorScheme.onPrimaryContainer
                )
                StatCard(
                    valor = resumen.stockBajo, etiqueta = "Stock bajo", icono = Icons.Default.Warning,
                    fondo = MaterialTheme.colorScheme.tertiaryContainer,
                    contenido = MaterialTheme.colorScheme.onTertiaryContainer
                )
                StatCard(
                    valor = resumen.agotados, etiqueta = "Agotados", icono = Icons.Default.Error,
                    fondo = MaterialTheme.colorScheme.errorContainer,
                    contenido = MaterialTheme.colorScheme.onErrorContainer
                )
            }

            Spacer(Modifier.height(4.dp))

            AccionCard(
                titulo = "Productos",
                descripcion = "Consultar, registrar y editar el inventario",
                icono = Icons.Default.Inventory2,
                colorIcono = MaterialTheme.colorScheme.primary,
                onClick = onProductos
            )
            AccionCard(
                titulo = "Escanear código QR",
                descripcion = "Identificar un producto con la cámara",
                icono = Icons.Default.QrCodeScanner,
                colorIcono = MaterialTheme.colorScheme.secondary,
                onClick = onEscanearQr
            )

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = viewModel::cargarEjemplos,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Cargar productos de ejemplo")
            }
            TextButton(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesión")
            }
            Spacer(Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun RowScope.StatCard(
    valor: Int,
    etiqueta: String,
    icono: ImageVector,
    fondo: Color,
    contenido: Color
) {
    Card(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = fondo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(icono, contentDescription = null, tint = contenido, modifier = Modifier.size(22.dp))
            Text(
                valor.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = contenido
            )
            Text(etiqueta, style = MaterialTheme.typography.labelMedium, color = contenido)
        }
    }
}

@Composable
private fun AccionCard(
    titulo: String,
    descripcion: String,
    icono: ImageVector,
    colorIcono: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(colorIcono),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}
