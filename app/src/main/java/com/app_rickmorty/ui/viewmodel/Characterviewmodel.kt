package com.app_rickmorty.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_rickmorty.data.local.AppPreferences
import com.app_rickmorty.data.model.CharacterUi
import com.app_rickmorty.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CharactersUiState {
    data object Loading : CharactersUiState
    data class Success(val characters: List<CharacterUi>) : CharactersUiState
    data class Error(val message: String) : CharactersUiState
}

/** Filtros activos para Portal Explorer. */
data class CharacterFilters(
    val status: String? = null,   // "alive" | "dead" | "unknown"
    val gender: String? = null,   // "male" | "female" | "genderless" | "unknown"
    val species: String = ""
) {
    val activeCount: Int
        get() = listOfNotNull(status, gender, species.takeIf { it.isNotBlank() }).size
}

/** Estado del scroll infinito de la pantalla Multiverso. */
data class MultiverseUiState(
    val characters: List<CharacterUi> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val endReached: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 0
)

class CharacterViewModel(
    private val repository: CharacterRepository = CharacterRepository(),
    private val preferences: AppPreferences? = null
) : ViewModel() {

    // ----- Portal Explorer: búsqueda + filtros -----
    private val _uiState = MutableStateFlow<CharactersUiState>(CharactersUiState.Loading)
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    private val _filters = MutableStateFlow(CharacterFilters())
    val filters: StateFlow<CharacterFilters> = _filters.asStateFlow()

    private var currentQuery: String = ""

    // Cache en memoria: evita re-pedir a la API un personaje que ya vimos
    private val characterCache = mutableMapOf<Int, CharacterUi>()

    // ----- Favoritos (persistidos vía AppPreferences) -----
    private val _favoriteIds = MutableStateFlow(preferences?.getFavoriteIds() ?: emptySet())
    val favoriteIds: StateFlow<Set<Int>> = _favoriteIds.asStateFlow()

    private val _favoritesState = MutableStateFlow<CharactersUiState>(CharactersUiState.Loading)
    val favoritesState: StateFlow<CharactersUiState> = _favoritesState.asStateFlow()

    // ----- Multiverso: todos los personajes, scroll infinito -----
    private val _multiverseState = MutableStateFlow(MultiverseUiState())
    val multiverseState: StateFlow<MultiverseUiState> = _multiverseState.asStateFlow()

    init {
        loadCharacters()
        refreshFavorites()
    }

    // ----- Portal -----

    fun loadCharacters() {
        currentQuery = ""
        fetchPortal()
    }

    fun search(query: String) {
        currentQuery = query
        fetchPortal()
    }

    fun applyFilters(newFilters: CharacterFilters) {
        _filters.value = newFilters
        fetchPortal()
    }

    private fun fetchPortal() {
        viewModelScope.launch {
            _uiState.value = CharactersUiState.Loading
            try {
                val f = _filters.value
                val characters = repository.getCharacters(
                    page = 1,
                    name = currentQuery.ifBlank { null },
                    status = f.status,
                    species = f.species.ifBlank { null },
                    gender = f.gender
                )
                characters.forEach { characterCache[it.id] = it }
                _uiState.value = CharactersUiState.Success(characters)
            } catch (e: Exception) {
                _uiState.value = CharactersUiState.Error(
                    e.localizedMessage ?: "No se encontraron resultados"
                )
            }
        }
    }

    // ----- Multiverso -----

    fun loadMultiverse(reset: Boolean = false) {
        val state = _multiverseState.value
        if (state.isLoading || state.isLoadingMore) return
        if (!reset && state.endReached) return

        val nextPage = if (reset) 1 else state.currentPage + 1

        viewModelScope.launch {
            _multiverseState.value = if (reset) {
                MultiverseUiState(isLoading = true)
            } else {
                state.copy(isLoadingMore = true, error = null)
            }
            try {
                val result = repository.getCharactersPage(nextPage)
                result.characters.forEach { characterCache[it.id] = it }
                val accumulated = if (reset) {
                    result.characters
                } else {
                    _multiverseState.value.characters + result.characters
                }
                _multiverseState.value = MultiverseUiState(
                    characters = accumulated,
                    isLoading = false,
                    isLoadingMore = false,
                    endReached = result.nextPage == null,
                    currentPage = nextPage
                )
            } catch (e: Exception) {
                _multiverseState.value = _multiverseState.value.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    error = e.localizedMessage ?: "No se pudo cargar el multiverso"
                )
            }
        }
    }

    // ----- Favoritos -----

    fun toggleFavorite(character: CharacterUi) {
        characterCache[character.id] = character
        val current = _favoriteIds.value
        val updated = if (current.contains(character.id)) {
            current - character.id
        } else {
            current + character.id
        }
        _favoriteIds.value = updated
        preferences?.saveFavoriteIds(updated)
        refreshFavorites()
    }

    private fun refreshFavorites() {
        val ids = _favoriteIds.value
        if (ids.isEmpty()) {
            _favoritesState.value = CharactersUiState.Success(emptyList())
            return
        }
        viewModelScope.launch {
            _favoritesState.value = CharactersUiState.Loading
            val missing = ids.filter { characterCache[it] == null }
            missing.forEach { id ->
                try {
                    characterCache[id] = repository.getCharacterById(id)
                } catch (_: Exception) {
                    // Si un favorito ya no existe en la API, simplemente se omite.
                }
            }
            val favorites = ids.mapNotNull { characterCache[it] }
            _favoritesState.value = CharactersUiState.Success(favorites)
        }
    }
}