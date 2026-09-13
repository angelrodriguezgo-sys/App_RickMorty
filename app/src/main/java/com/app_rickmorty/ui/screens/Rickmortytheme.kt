package com.app_rickmorty.ui.screens



import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Paleta base
val SpaceBlack = Color(0xFF06070B)
val PanelDark = Color(0xFF14161C)
val PanelDarkAlt = Color(0xFF1B1E26)
val NeonGreen = Color(0xFF39FF14)
val NeonGreenSoft = Color(0xFF7CFF6B)
val CyanAccent = Color(0xFF3DE8FF)
val StatusGreen = Color(0xFF2ECC71)
val StatusYellow = Color(0xFFF5C518)
val TextPrimary = Color(0xFFF5F5F5)
val TextSecondary = Color(0xFFA0A4AE)
val BorderSubtle = Color(0xFF2A2D36)

// Fondo estilo "espacio" para welcome-screen
val SpaceBackgroundBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFF03040A), Color(0xFF0A0F0C), Color(0xFF03040A))
)

// Glow radial detrás del portal / vórtice
val PortalGlowBrush = Brush.radialGradient(
    colors = listOf(NeonGreenSoft.copy(alpha = 0.55f), NeonGreen.copy(alpha = 0.15f), Color.Transparent)
)