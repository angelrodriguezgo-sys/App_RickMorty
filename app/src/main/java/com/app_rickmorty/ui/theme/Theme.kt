package com.app_rickmorty.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp


private val RickMortyColorScheme = darkColorScheme(
    primary = PortalGreen,
    onPrimary = PortalBlack,
    secondary = MultiverseLavender,
    onSecondary = PortalBlack,
    tertiary = DangerAmber,
    background = PortalBlack,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceDarkElevated,
    onSurfaceVariant = TextSecondary,
    outline = DividerColor
)

private val RickMortyShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(50)
)


@Composable
fun App_RickMortyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RickMortyColorScheme,
        typography = RickMortyTypography,
        shapes = RickMortyShapes,
        content = content
    )
}