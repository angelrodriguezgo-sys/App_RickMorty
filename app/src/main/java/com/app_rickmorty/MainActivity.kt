package com.app_rickmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app_rickmorty.data.local.AppPreferences
import com.app_rickmorty.data.model.CharacterUi
import com.app_rickmorty.ui.screens.CharacterDetailScreen
import com.app_rickmorty.ui.screens.FavoritesScreen
import com.app_rickmorty.ui.screens.FilterDialog
import com.app_rickmorty.ui.screens.MultiverseScreen
import com.app_rickmorty.ui.screens.PortalExplorerScreen
import com.app_rickmorty.ui.screens.ProfileScreen
import com.app_rickmorty.ui.screens.WelcomeScreen
import com.app_rickmorty.ui.theme.App_RickMortyTheme
import com.app_rickmorty.ui.viewmodel.CharacterViewModel
import com.app_rickmorty.ui.viewmodel.ProfileViewModel

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
private enum class AppScreen { WELCOME, PORTAL, MULTIVERSE, FAVORITES, PROFILE, DETAIL }

private fun screenForTab(tab: Int): AppScreen = when (tab) {
    0 -> AppScreen.PORTAL
    1 -> AppScreen.MULTIVERSE
    2 -> AppScreen.FAVORITES
    else -> AppScreen.PROFILE
}

/** Factory manual: crea CharacterViewModel/ProfileViewModel pasándoles AppPreferences (necesita Context). */
private class AppViewModelFactory(
    private val preferences: AppPreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CharacterViewModel::class.java -> CharacterViewModel(preferences = preferences) as T
            ProfileViewModel::class.java -> ProfileViewModel(preferences = preferences) as T
            else -> throw IllegalArgumentException("ViewModel no soportado: ${modelClass.name}")
        }
    }
}

@Composable
private fun RickMortyApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val preferences = remember { AppPreferences(context) }
    val factory = remember { AppViewModelFactory(preferences) }

    val viewModel: CharacterViewModel = viewModel(factory = factory)
    val profileViewModel: ProfileViewModel = viewModel(factory = factory)

    var currentScreen by remember { mutableStateOf(AppScreen.WELCOME) }
    var selectedTab by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCharacter by remember { mutableStateOf<CharacterUi?>(null) }
    var showFilterDialog by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filters by viewModel.filters.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteIds.collectAsStateWithLifecycle()
    val multiverseState by viewModel.multiverseState.collectAsStateWithLifecycle()
    val favoritesState by viewModel.favoritesState.collectAsStateWithLifecycle()
    val profileState by profileViewModel.uiState.collectAsStateWithLifecycle()

    val goToTab: (Int) -> Unit = { tab ->
        selectedTab = tab
        currentScreen = screenForTab(tab)
    }

    if (showFilterDialog) {
        FilterDialog(
            currentFilters = filters,
            onDismiss = { showFilterDialog = false },
            onApply = { viewModel.applyFilters(it) }
        )
    }

    when (currentScreen) {
        AppScreen.WELCOME -> WelcomeScreen(
            onIniciar = {
                selectedTab = 0
                currentScreen = AppScreen.PORTAL
            },
            onConfiguracion = {
                selectedTab = 3
                currentScreen = AppScreen.PROFILE
            }
        )

        AppScreen.PORTAL -> PortalExplorerScreen(
            uiState = uiState,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onSearchSubmit = { viewModel.search(searchQuery) },
            onFiltrosClick = { showFilterDialog = true },
            activeFilterCount = filters.activeCount,
            favoriteIds = favoriteIds,
            onToggleFavorite = { viewModel.toggleFavorite(it) },
            selectedTab = selectedTab,
            onTabSelected = goToTab,
            onCharacterClick = { character ->
                selectedCharacter = character
                currentScreen = AppScreen.DETAIL
            }
        )

        AppScreen.MULTIVERSE -> {
            LaunchedEffect(Unit) {
                if (multiverseState.characters.isEmpty() && !multiverseState.isLoading) {
                    viewModel.loadMultiverse(reset = true)
                }
            }
            MultiverseScreen(
                state = multiverseState,
                favoriteIds = favoriteIds,
                onLoadMore = { viewModel.loadMultiverse() },
                onCharacterClick = { character ->
                    selectedCharacter = character
                    currentScreen = AppScreen.DETAIL
                },
                onToggleFavorite = { viewModel.toggleFavorite(it) },
                selectedTab = selectedTab,
                onTabSelected = goToTab
            )
        }

        AppScreen.FAVORITES -> FavoritesScreen(
            state = favoritesState,
            onCharacterClick = { character ->
                selectedCharacter = character
                currentScreen = AppScreen.DETAIL
            },
            onToggleFavorite = { viewModel.toggleFavorite(it) },
            selectedTab = selectedTab,
            onTabSelected = goToTab
        )

        AppScreen.PROFILE -> ProfileScreen(
            state = profileState,
            onSaveUsername = { profileViewModel.saveUsername(it) },
            onRegenerate = { profileViewModel.regenerateCoordinates() },
            selectedTab = selectedTab,
            onTabSelected = goToTab
        )

        AppScreen.DETAIL -> {
            selectedCharacter?.let { character ->
                CharacterDetailScreen(
                    character = character,
                    isFavorite = favoriteIds.contains(character.id),
                    onBack = { currentScreen = screenForTab(selectedTab) },
                    onToggleFavorite = { viewModel.toggleFavorite(character) }
                )
            }
        }
    }
}