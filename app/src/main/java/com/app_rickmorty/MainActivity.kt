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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app_rickmorty.data.model.CharacterUi
import com.app_rickmorty.ui.viewmodel.CharacterViewModel
import com.app_rickmorty.ui.viewmodel.CharactersUiState
import com.app_rickmorty.ui.screens.CharacterDetailScreen
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
private enum class AppScreen { WELCOME, PORTAL, DETAIL }

@Composable
private fun RickMortyApp(
    modifier: Modifier = Modifier,
    viewModel: CharacterViewModel = viewModel()
) {
    var currentScreen by remember { mutableStateOf(AppScreen.WELCOME) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCharacter by remember { mutableStateOf<CharacterUi?>(null) }

    // Se actualiza solo cada vez que el ViewModel recibe datos de rickandmortyapi.com
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (currentScreen) {
        AppScreen.WELCOME -> WelcomeScreen(
            onIniciar = { currentScreen = AppScreen.PORTAL },
            onConfiguracion = { /* TODO: pantalla de configuración */ }
        )

        AppScreen.PORTAL -> PortalExplorerScreen(
            uiState = uiState,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onSearchSubmit = { viewModel.search(searchQuery) },
            onFiltrosClick = { /* TODO: abrir filtros */ },
            onCharacterClick = { character ->
                selectedCharacter = character
                currentScreen = AppScreen.DETAIL
            }
        )

        AppScreen.DETAIL -> {
            selectedCharacter?.let { character ->
                CharacterDetailScreen(
                    character = character,
                    onBack = { currentScreen = AppScreen.PORTAL },
                    onAddToFavorites = { /* TODO: guardar en favoritos */ }
                )
            }
        }
    }
}