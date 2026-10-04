package com.app_rickmorty.data.repository



import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await


/** Envuelve FirebaseAuth: login, registro y cierre de sesión. */
class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun login(email: String, password: String): FirebaseUser {
        val result = auth.signInWithEmailAndPassword(email.trim(), password).await()
        return result.user ?: throw IllegalStateException("No se pudo iniciar sesión")
    }

    suspend fun register(email: String, password: String): FirebaseUser {
        val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
        return result.user ?: throw IllegalStateException("No se pudo crear la cuenta")
    }

    fun logout() {
        auth.signOut()
    }
}