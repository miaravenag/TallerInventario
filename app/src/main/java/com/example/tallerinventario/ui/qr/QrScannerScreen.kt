package com.example.tallerinventario.ui.qr

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.tallerinventario.data.local.ProductoEntity
import com.example.tallerinventario.ui.producto.EstadoStockBadge
import com.example.tallerinventario.ui.producto.estadoStock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScannerScreen(
    viewModel: QrViewModel,
    onVolver: () -> Unit,
    onEditar: (Long) -> Unit,
    onRegistrar: (String) -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val context = LocalContext.current

    var tienePermiso by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }
    val lanzadorPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido -> tienePermiso = concedido }

    LaunchedEffect(Unit) {
        if (!tienePermiso) lanzadorPermiso.launch(Manifest.permission.CAMERA)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escanear código QR") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val producto = estado.producto

            when {
                estado.escaneando && !tienePermiso -> {
                    Text("Permiso de cámara requerido", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "Para leer códigos QR necesitamos acceso a la cámara.",
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = { lanzadorPermiso.launch(Manifest.permission.CAMERA) }) {
                        Text("Conceder permiso de cámara")
                    }
                }

                estado.escaneando -> {
                    Text("Enfoca el código QR del producto", style = MaterialTheme.typography.titleMedium)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        QrScanner(
                            onQrCodeScanned = viewModel::onQrDetectado,
                            modifier = Modifier.fillMaxSize()
                        )
                        // Marco guía al centro
                        Box(
                            modifier = Modifier
                                .size(250.dp)
                                .align(Alignment.Center)
                                .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                        )
                    }
                }

                estado.buscando -> CircularProgressIndicator()

                producto != null -> ResultadoProducto(
                    producto = producto,
                    onEditar = { onEditar(producto.id) },
                    onOtro = viewModel::reiniciar
                )

                else -> {
                    Text("Producto no registrado", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "El código \"${estado.codigoLeido}\" no existe en el inventario.",
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = { onRegistrar(estado.codigoLeido ?: "") },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Registrar con este código") }
                    OutlinedButton(
                        onClick = viewModel::reiniciar,
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Escanear otro código") }
                }
            }
        }
    }
}

@Composable
private fun ResultadoProducto(
    producto: ProductoEntity,
    onEditar: () -> Unit,
    onOtro: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(producto.nombre, style = MaterialTheme.typography.titleLarge)
            Text("Código: ${producto.codigo}")
            Text("Categoría: ${producto.categoria}")
            Text("Cantidad disponible: ${producto.cantidad}")
            Text("Existencia mínima: ${producto.stockMinimo}")
            Spacer(Modifier.height(4.dp))
            EstadoStockBadge(producto.estadoStock())
        }
    }
    Button(onClick = onEditar, modifier = Modifier.fillMaxWidth()) { Text("Ver / editar producto") }
    OutlinedButton(onClick = onOtro, modifier = Modifier.fillMaxWidth()) { Text("Escanear otro código") }
}
