package com.example.tallerinventario.ui.producto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tallerinventario.data.local.ProductoEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaProductosScreen(
    viewModel: ProductoViewModel,
    onAgregar: () -> Unit,
    onEditar: (Long) -> Unit,
    onEscanearQr: () -> Unit,
    onVolver: () -> Unit
) {
    val estado by viewModel.listaState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onEscanearQr) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = "Escanear código QR")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAgregar,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Agregar") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = estado.busqueda,
                onValueChange = viewModel::onBusquedaChange,
                label = { Text("Buscar por nombre o código") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            if (estado.productos.isEmpty() && !estado.cargando) {
                Text(
                    text = if (estado.busqueda.isBlank())
                        "Aún no hay productos registrados. Usa el botón Agregar."
                    else
                        "No se encontraron productos para la búsqueda.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 24.dp)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(estado.productos, key = { it.id }) { producto ->
                        ProductoItem(producto = producto, onClick = { onEditar(producto.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductoItem(producto: ProductoEntity, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
            Text("Código: ${producto.codigo}  ·  ${producto.categoria}", style = MaterialTheme.typography.bodyMedium)
            Text(
                "Stock: ${producto.cantidad}  (mínimo ${producto.stockMinimo})",
                style = MaterialTheme.typography.bodyMedium
            )
            EstadoStockBadge(producto.estadoStock())
        }
    }
}
