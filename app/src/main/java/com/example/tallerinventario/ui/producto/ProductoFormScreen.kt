package com.example.tallerinventario.ui.producto

import com.example.tallerinventario.ui.theme.appTopBarColors
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(
    viewModel: ProductoViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.formState.collectAsState()
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    // Cuando el ViewModel indica que se guardó/eliminó, limpiamos y volvemos a la lista
    LaunchedEffect(estado.guardadoExitosamente) {
        if (estado.guardadoExitosamente) {
            viewModel.limpiarFormulario()
            onVolver()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = appTopBarColors(),
                title = { Text(if (estado.esEdicion) "Editar producto" else "Nuevo producto") },
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
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CampoTexto(estado.codigo, viewModel::onCodigoChange, "Código", estado.codigoError)
            CampoTexto(estado.nombre, viewModel::onNombreChange, "Nombre", estado.nombreError)
            CampoTexto(estado.categoria, viewModel::onCategoriaChange, "Categoría", estado.categoriaError)
            CampoTexto(
                estado.cantidad, viewModel::onCantidadChange, "Cantidad", estado.cantidadError,
                KeyboardType.Number
            )
            CampoTexto(
                estado.stockMinimo, viewModel::onStockMinimoChange, "Existencia mínima",
                estado.stockMinimoError, KeyboardType.Number
            )

            estado.errorGeneral?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }

            Button(
                onClick = viewModel::guardar,
                enabled = !estado.guardando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (estado.guardando) {
                    CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text(if (estado.esEdicion) "Guardar cambios" else "Registrar producto")
                }
            }

            OutlinedButton(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
                Text("Cancelar")
            }

            if (estado.esEdicion) {
                TextButton(
                    onClick = { mostrarConfirmacion = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar producto", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            title = { Text("Eliminar producto") },
            text = { Text("¿Seguro que quieres eliminar \"${estado.nombre}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarConfirmacion = false
                    viewModel.eliminar()
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacion = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun CampoTexto(
    valor: String,
    onChange: (String) -> Unit,
    etiqueta: String,
    error: String?,
    tipoTeclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(etiqueta) },
        isError = error != null,
        supportingText = { Text(error ?: "") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
        modifier = Modifier.fillMaxWidth()
    )
}
