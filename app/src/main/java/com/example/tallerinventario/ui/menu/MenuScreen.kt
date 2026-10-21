package com.example.tallerinventario.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onProductos: () -> Unit,
    onEscanearQr: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Scaffold(topBar = { TopAppBar(title = { Text("Menú principal") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = onProductos, modifier = Modifier.fillMaxWidth()) {
                Text("Productos")
            }
            Button(onClick = onEscanearQr, modifier = Modifier.fillMaxWidth()) {
                Text("Escanear código QR")
            }
            OutlinedButton(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth()) {
                Text("Cerrar sesión")
            }
        }
    }
}
