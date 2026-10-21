package com.example.tallerinventario.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(viewModel: LoginViewModel, onLoginExitoso: () -> Unit) {
    val estado by viewModel.estado.collectAsState()

    LaunchedEffect(estado.loginExitoso) {
        if (estado.loginExitoso) onLoginExitoso()
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            Text("Inventario del Taller", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Ingresa con la cuenta de demostración",
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = estado.usuario,
                onValueChange = viewModel::onUsuarioChange,
                label = { Text("Usuario") },
                isError = estado.usuarioError != null,
                supportingText = { Text(estado.usuarioError ?: "") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = estado.clave,
                onValueChange = viewModel::onClaveChange,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = estado.claveError != null,
                supportingText = { Text(estado.claveError ?: "") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            estado.errorGeneral?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Button(onClick = viewModel::iniciarSesion, modifier = Modifier.fillMaxWidth()) {
                Text("Ingresar")
            }
        }
    }
}
