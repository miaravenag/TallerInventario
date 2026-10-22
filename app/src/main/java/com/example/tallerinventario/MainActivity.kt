package com.example.tallerinventario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tallerinventario.ui.navigation.AppNavigation
import com.example.tallerinventario.ui.theme.TallerInventarioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Barra de estado transparente con íconos claros (el encabezado es azul marino)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            TallerInventarioTheme {
                AppNavigation()
            }
        }
    }
}
