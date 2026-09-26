package com.app_rickmorty.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.app_rickmorty.data.local.AppPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

data class ProfileUiState(
    val username: String = "",
    val dimension: String = "",
    val tierra: String = ""
)

class ProfileViewModel(
    private val preferences: AppPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(loadInitialState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private fun loadInitialState(): ProfileUiState {
        val storedDimension = preferences.getDimension()
        val storedTierra = preferences.getTierra()

        val dimension: String
        val tierra: String
        if (storedDimension != null && storedTierra != null) {
            dimension = storedDimension
            tierra = storedTierra
        } else {
            dimension = randomDimension()
            tierra = randomTierra()
            preferences.saveDimensionAndTierra(dimension, tierra)
        }

        return ProfileUiState(
            username = preferences.getUsername(),
            dimension = dimension,
            tierra = tierra
        )
    }

    fun saveUsername(name: String) {
        preferences.saveUsername(name)
        _uiState.value = _uiState.value.copy(username = name)
    }

    fun regenerateCoordinates() {
        val dimension = randomDimension()
        val tierra = randomTierra()
        preferences.saveDimensionAndTierra(dimension, tierra)
        _uiState.value = _uiState.value.copy(dimension = dimension, tierra = tierra)
    }

    private fun randomDimension(): String {
        val letter = ('A'..'Z').random()
        val number = Random.nextInt(1, 999)
        return "Dimensión $letter-$number"
    }

    private fun randomTierra(): String {
        val number = Random.nextInt(1, 99999)
        return "Tierra-$number"
    }
}