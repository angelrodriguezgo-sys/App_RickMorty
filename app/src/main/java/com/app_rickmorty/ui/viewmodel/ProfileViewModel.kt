package com.app_rickmorty.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app_rickmorty.data.local.AppPreferences
import com.app_rickmorty.data.repository.AuthRepository
import com.app_rickmorty.data.repository.UserProfile
import com.app_rickmorty.data.repository.UserRepository
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.random.Random

/** Si Firestore no responde en este tiempo, se cancela y se muestra un error (en vez de quedar cargando para siempre). */
private const val FIRESTORE_TIMEOUT_MS = 15_000L

data class ProfileUiState(
    val isLoading: Boolean = true,
    val username: String = "",
    val dimension: String = "",
    val tierra: String = "",
    val email: String = "",
    val photoBase64: String = "",
    val isUploadingPhoto: Boolean = false,
    val isSavingUsername: Boolean = false,
    val isSavingCoordinates: Boolean = false,
    val errorMessage: String? = null
)

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

    fun loadProfile() {
        val user = authRepository.currentUser
        if (user == null) {
            _uiState.value = ProfileUiState(isLoading = false)
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val existing = withTimeout(FIRESTORE_TIMEOUT_MS) { userRepository.getProfile(user.uid) }
                val profile = existing ?: UserProfile(
                    uid = user.uid,
                    email = user.email.orEmpty(),
                    username = user.email.orEmpty().substringBefore("@"),
                    dimension = randomDimension(),
                    tierra = randomTierra()
                ).also { withTimeout(FIRESTORE_TIMEOUT_MS) { userRepository.saveProfile(it) } }

                _uiState.value = ProfileUiState(
                    isLoading = false,
                    username = profile.username,
                    dimension = profile.dimension,
                    tierra = profile.tierra,
                    email = profile.email.ifBlank { user.email.orEmpty() },
                    photoBase64 = profile.photoBase64
                )
            } catch (e: TimeoutCancellationException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "No hay conexión con el servidor (tardó demasiado). Revisa tu internet e intenta de nuevo."
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "No se pudo cargar tu perfil: ${e.localizedMessage ?: "revisa tu conexión"}"
                )
            }
        }
    }

    /** Guarda el nombre de usuario (también funciona como tu alias visible). */
    fun saveUsername(name: String) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "El nombre no puede quedar vacío")
            return
        }
        val uid = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSavingUsername = true, errorMessage = null)
            try {
                withTimeout(FIRESTORE_TIMEOUT_MS) { userRepository.updateUsername(uid, trimmed) }
                _uiState.value = _uiState.value.copy(username = trimmed, isSavingUsername = false)
            } catch (e: TimeoutCancellationException) {
                _uiState.value = _uiState.value.copy(
                    isSavingUsername = false,
                    errorMessage = "No se pudo guardar: no hay conexión con el servidor. Revisa tu internet e intenta de nuevo."
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSavingUsername = false,
                    errorMessage = "No se pudo guardar el nombre: ${e.localizedMessage ?: "revisa tu conexión"}"
                )
            }
        }
    }

    /** Genera y guarda una nueva dimensión + tierra (la sección morada del perfil). */
    fun regenerateCoordinates() {
        val uid = authRepository.currentUser?.uid ?: return
        val dimension = randomDimension()
        val tierra = randomTierra()
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSavingCoordinates = true, errorMessage = null)
            try {
                withTimeout(FIRESTORE_TIMEOUT_MS) { userRepository.updateDimension(uid, dimension, tierra) }
                _uiState.value = _uiState.value.copy(
                    dimension = dimension,
                    tierra = tierra,
                    isSavingCoordinates = false
                )
            } catch (e: TimeoutCancellationException) {
                _uiState.value = _uiState.value.copy(
                    isSavingCoordinates = false,
                    errorMessage = "No se pudo generar: no hay conexión con el servidor. Revisa tu internet e intenta de nuevo."
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSavingCoordinates = false,
                    errorMessage = "No se pudieron generar nuevas coordenadas: ${e.localizedMessage ?: "revisa tu conexión"}"
                )
            }
        }
    }

    /** Llamar con el Base64 ya generado por ImageUtils.uriToBase64(context, uri). */
    fun updateProfilePhoto(base64Photo: String) {
        val uid = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploadingPhoto = true, errorMessage = null)
            try {
                withTimeout(FIRESTORE_TIMEOUT_MS) { userRepository.updatePhoto(uid, base64Photo) }
                _uiState.value = _uiState.value.copy(photoBase64 = base64Photo, isUploadingPhoto = false)
            } catch (e: TimeoutCancellationException) {
                _uiState.value = _uiState.value.copy(
                    isUploadingPhoto = false,
                    errorMessage = "No se pudo subir la foto: no hay conexión con el servidor."
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUploadingPhoto = false,
                    errorMessage = "No se pudo actualizar la foto: ${e.localizedMessage ?: "revisa tu conexión"}"
                )
            }
        }
    }

    /** Elimina la foto de perfil y vuelve al ícono por defecto. */
    fun removeProfilePhoto() {
        val uid = authRepository.currentUser?.uid ?: return
        if (_uiState.value.photoBase64.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploadingPhoto = true, errorMessage = null)
            try {
                withTimeout(FIRESTORE_TIMEOUT_MS) { userRepository.removePhoto(uid) }
                _uiState.value = _uiState.value.copy(photoBase64 = "", isUploadingPhoto = false)
            } catch (e: TimeoutCancellationException) {
                _uiState.value = _uiState.value.copy(
                    isUploadingPhoto = false,
                    errorMessage = "No se pudo eliminar la foto: no hay conexión con el servidor."
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUploadingPhoto = false,
                    errorMessage = "No se pudo eliminar la foto: ${e.localizedMessage ?: "revisa tu conexión"}"
                )
            }
        }
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

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