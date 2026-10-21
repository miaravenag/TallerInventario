package com.example.tallerinventario.ui.login

import androidx.lifecycle.ViewModel
import com.example.tallerinventario.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val usuario: String = "",
    val clave: String = "",
    val usuarioError: String? = null,
    val claveError: String? = null,
    val errorGeneral: String? = null,
    val loginExitoso: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado.asStateFlow()

    fun onUsuarioChange(valor: String) =
        _estado.update { it.copy(usuario = valor, usuarioError = null, errorGeneral = null) }

    fun onClaveChange(valor: String) =
        _estado.update { it.copy(clave = valor, claveError = null, errorGeneral = null) }

    fun iniciarSesion() {
        val s = _estado.value
        val usuarioError = if (s.usuario.isBlank()) "Ingresa tu usuario" else null
        val claveError = if (s.clave.isBlank()) "Ingresa tu contraseña" else null

        if (usuarioError != null || claveError != null) {
            _estado.update { it.copy(usuarioError = usuarioError, claveError = claveError) }
            return
        }
        if (repository.autenticar(s.usuario, s.clave)) {
            _estado.update { it.copy(loginExitoso = true) }
        } else {
            _estado.update { it.copy(errorGeneral = "Usuario o contraseña incorrectos") }
        }
    }

    fun reiniciar() {
        _estado.value = LoginUiState()
    }
}
