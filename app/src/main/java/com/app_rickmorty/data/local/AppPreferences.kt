package com.app_rickmorty.data.local

import android.content.Context
import android.content.SharedPreferences

private const val PREFS_NAME = "rick_morty_prefs"
private const val KEY_FAVORITES = "favorite_ids"
private const val KEY_USERNAME = "profile_username"
private const val KEY_DIMENSION = "profile_dimension"
private const val KEY_TIERRA = "profile_tierra"

/**
 * Envoltorio simple sobre SharedPreferences: persiste favoritos y el perfil
 * localmente en el dispositivo (no requiere backend propio).
 */
class AppPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // ----- Favoritos -----
    fun getFavoriteIds(): Set<Int> {
        val raw = prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
        return raw.mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun saveFavoriteIds(ids: Set<Int>) {
        prefs.edit()
            .putStringSet(KEY_FAVORITES, ids.map { it.toString() }.toSet())
            .apply()
    }

    // ----- Perfil -----
    fun getUsername(): String = prefs.getString(KEY_USERNAME, "") ?: ""

    fun saveUsername(name: String) {
        prefs.edit().putString(KEY_USERNAME, name).apply()
    }

    fun getDimension(): String? = prefs.getString(KEY_DIMENSION, null)

    fun getTierra(): String? = prefs.getString(KEY_TIERRA, null)

    fun saveDimensionAndTierra(dimension: String, tierra: String) {
        prefs.edit()
            .putString(KEY_DIMENSION, dimension)
            .putString(KEY_TIERRA, tierra)
            .apply()
    }
}