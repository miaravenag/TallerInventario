package com.example.tallerinventario.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val AzulMarino = Color(0xFF1F3A5F)
val AzulOscuro = Color(0xFF14284A)
val Naranja = Color(0xFFE8590C)

private val EsquemaClaro = lightColorScheme(
    primary = AzulMarino,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD9E6FA),
    onPrimaryContainer = Color(0xFF0B1F3A),
    secondary = Naranja,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0D0),
    onSecondaryContainer = Color(0xFF3A1300),
    tertiary = Color(0xFF9A5B00),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFE8C2),
    onTertiaryContainer = Color(0xFF2E1A00),
    error = Color(0xFFBA1A1A),
    background = Color(0xFFF4F6FA),
    onBackground = Color(0xFF14181F),
    surface = Color.White,
    onSurface = Color(0xFF14181F),
    surfaceVariant = Color(0xFFE4E9F2),
    onSurfaceVariant = Color(0xFF44505F),
    outline = Color(0xFF7A8594),
    outlineVariant = Color(0xFFC9D1DD)
)

private val FormasApp = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

/** Tema único (claro) para mantener un aspecto consistente. */
@Composable
fun TallerInventarioTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EsquemaClaro, shapes = FormasApp, content = content)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun appTopBarColors(): TopAppBarColors = TopAppBarDefaults.topAppBarColors(
    containerColor = AzulMarino,
    titleContentColor = Color.White,
    navigationIconContentColor = Color.White,
    actionIconContentColor = Color.White
)
