package com.app_rickmorty.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await



/** Perfil del usuario guardado en Firestore (colección "users"), ligado al UID de la cuenta. */
data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val dimension: String = "",
    val tierra: String = "",
    // Foto de perfil codificada en Base64 (ya comprimida y pequeña).
    // Evitamos Cloud Storage porque desde feb-2026 exige el plan Blaze.
    val photoBase64: String = ""
)

class UserRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private fun userDoc(uid: String) = firestore.collection("users").document(uid)

    suspend fun getProfile(uid: String): UserProfile? {
        return userDoc(uid).get().await().toObject(UserProfile::class.java)
    }

    suspend fun saveProfile(profile: UserProfile) {
        userDoc(profile.uid).set(profile, SetOptions.merge()).await()
    }

    suspend fun updateUsername(uid: String, username: String) {
        userDoc(uid).set(mapOf("username" to username), SetOptions.merge()).await()
    }

    suspend fun updateDimension(uid: String, dimension: String, tierra: String) {
        userDoc(uid).set(mapOf("dimension" to dimension, "tierra" to tierra), SetOptions.merge()).await()
    }

    suspend fun updatePhoto(uid: String, base64Photo: String) {
        userDoc(uid).set(mapOf("photoBase64" to base64Photo), SetOptions.merge()).await()
    }

    suspend fun removePhoto(uid: String) {
        updatePhoto(uid, "")
    }
}