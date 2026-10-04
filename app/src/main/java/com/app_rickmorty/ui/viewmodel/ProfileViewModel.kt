package com.app_rickmorty.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_rickmorty.data.local.AppPreferences
import com.app_rickmorty.data.repository.AuthRepository
import com.app_rickmorty.data.repository.UserProfile
import com.app_rickmorty.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

data class ProfileUiState(
    val isLoading: Boolean = true,
    val username: String = "",
    val dimension: String = "",
    val tierra: String = "",
    val email: String = "",
    val photoBase64: String = "",
    val isUploadingPhoto: Boolean = false
)

/**
 * Nota: [preferences] ya no se usa para username/dimensión/tierra (eso ahora vive
 * en Firestore, atado a la cuenta de Firebase). Se mantiene el parámetro para no
 * romper el AppViewModelFactory existente; si quieres, puedes quitarlo más adelante.
 */
class ProfileViewModel(
    private val preferences: AppPreferences,
    private val authRepository: AuthRepository = AuthRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    /**
     * Carga (o crea, si es la primera vez) el perfil del usuario de Firebase
     * actualmente logueado. Llamar también justo después de un login/registro
     * exitoso, para refrescar si la sesión cambió a otra cuenta.
     */
    fun loadProfile() {
        val user = authRepository.currentUser
        if (user == null) {
            _uiState.value = ProfileUiState(isLoading = false)
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val existing = userRepository.getProfile(user.uid)
                val profile = existing ?: UserProfile(
                    uid = user.uid,
                    email = user.email.orEmpty(),
                    username = user.email.orEmpty().substringBefore("@"),
                    dimension = randomDimension(),
                    tierra = randomTierra()
                ).also { userRepository.saveProfile(it) }

                _uiState.value = ProfileUiState(
                    isLoading = false,
                    username = profile.username,
                    dimension = profile.dimension,
                    tierra = profile.tierra,
                    email = profile.email
                        .ifBlank { user.email.orEmpty() },
                    photoBase64 = profile.photoBase64
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun saveUsername(name: String) {
        val uid = authRepository.currentUser?.uid ?: return
        _uiState.value = _uiState.value.copy(username = name)
        viewModelScope.launch {
            try {
                userRepository.updateUsername(uid, name)
            } catch (_: Exception) {
                // Podrías exponer un estado de error aquí si quieres mostrarlo.
            }
        }
    }

    fun regenerateCoordinates() {
        val uid = authRepository.currentUser?.uid ?: return
        val dimension = randomDimension()
        val tierra = randomTierra()
        _uiState.value = _uiState.value.copy(dimension = dimension, tierra = tierra)
        viewModelScope.launch {
            try {
                userRepository.updateDimension(uid, dimension, tierra)
            } catch (_: Exception) {
            }
        }
    }

    /** Llamar con el Base64 ya generado por ImageUtils.uriToBase64(context, uri). */
    fun updateProfilePhoto(base64Photo: String) {
        val uid = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploadingPhoto = true)
            try {
                userRepository.updatePhoto(uid, base64Photo)
                _uiState.value = _uiState.value.copy(photoBase64 = base64Photo)
            } catch (_: Exception) {
            } finally {
                _uiState.value = _uiState.value.copy(isUploadingPhoto = false)
            }
        }
    }

    /**
     * Cierra sesión y limpia el estado en memoria, para no arrastrar los datos
     * de la cuenta anterior mientras se carga la siguiente (o se muestra el login).
     */
    fun clearOnLogout() {
        authRepository.logout()
        _uiState.value = ProfileUiState(isLoading = false)
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