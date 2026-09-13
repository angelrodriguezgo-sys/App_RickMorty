package com.app_rickmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.app_rickmorty.ui.screens.PortalExplorerScreen
import com.app_rickmorty.ui.screens.WelcomeScreen
import com.app_rickmorty.ui.theme.App_RickMortyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App_RickMortyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RickMortyApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/**
 * Navegación simple sin dependencia de Navigation-Compose:
 * un estado que decide qué pantalla se dibuja.
 */
private enum class AppScreen { WELCOME, PORTAL }

@Composable
private fun RickMortyApp(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf(AppScreen.WELCOME) }
    var searchQuery by remember { mutableStateOf("") }

    when (currentScreen) {
        AppScreen.WELCOME -> WelcomeScreen(
            onIniciar = { currentScreen = AppScreen.PORTAL },
            onConfiguracion = { /* TODO: pantalla de configuración */ }
        )
        AppScreen.PORTAL -> PortalExplorerScreen(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onFiltrosClick = { /* TODO: abrir filtros */ }
        )
    }
}