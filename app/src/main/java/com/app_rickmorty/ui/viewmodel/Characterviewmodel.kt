package com.app_rickmorty.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class CharacterViewModel(
    private val repository: CharacterRepository = CharacterRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<CharactersUiState>(CharactersUiState.Loading)
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch {
            _uiState.value = CharactersUiState.Loading
            try {
                val characters = repository.getCharacters(page = 1)
                _uiState.value = CharactersUiState.Success(characters)
            } catch (e: Exception) {
                _uiState.value = CharactersUiState.Error(
                    e.localizedMessage ?: "No se pudo conectar con la API"
                )
            }
        }
    }

    fun search(query: String) {
        if (query.isBlank()) {
            loadCharacters()
            return
        }
        viewModelScope.launch {
            _uiState.value = CharactersUiState.Loading
            try {
                val characters = repository.searchByName(query)
                _uiState.value = CharactersUiState.Success(characters)
            } catch (e: Exception) {
                _uiState.value = CharactersUiState.Error(
                    e.localizedMessage ?: "No se encontraron resultados"
                )
            }
        }
    }
}