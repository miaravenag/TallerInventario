package com.example.tallerinventario.ui.qr

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tallerinventario.data.local.AppDatabase
import com.example.tallerinventario.data.local.ProductoEntity
import com.example.tallerinventario.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QrUiState(
    val escaneando: Boolean = true,
    val buscando: Boolean = false,
    val codigoLeido: String? = null,
    val producto: ProductoEntity? = null   // null + buscando=false + codigoLeido!=null => no registrado
)

class QrViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = ProductoRepository(AppDatabase.getDatabase(app).productoDao())

    private val _estado = MutableStateFlow(QrUiState())
    val estado: StateFlow<QrUiState> = _estado.asStateFlow()

    /** Se llama cuando la cámara detecta un QR. El contenido del QR es el código interno. */
    fun onQrDetectado(contenido: String) {
        if (!_estado.value.escaneando) return
        val codigo = contenido.trim().uppercase()
        _estado.value = QrUiState(escaneando = false, buscando = true, codigoLeido = codigo)

        viewModelScope.launch {
            val producto = repository.obtenerPorCodigo(codigo)
            _estado.update { it.copy(buscando = false, producto = producto) }
        }
    }

    fun reiniciar() {
        _estado.value = QrUiState()
    }
}
