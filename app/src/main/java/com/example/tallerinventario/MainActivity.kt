package com.example.tallerinventario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tallerinventario.ui.navigation.AppNavigation
import com.example.tallerinventario.ui.theme.TallerInventarioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TallerInventarioTheme {
                AppNavigation()
            }
        }
    }
}