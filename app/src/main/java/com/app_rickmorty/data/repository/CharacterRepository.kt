package com.app_rickmorty.data.repository;


import com.app_rickmorty.data.model.CharacterUi
import com.app_rickmorty.data.model.toCharacterUi
import com.app_rickmorty.data.remote.RetrofitClient
import com.app_rickmorty.data.remote.RickAndMortyApi

class CharacterRepository(
    private val api: RickAndMortyApi = RetrofitClient.api
) {
    suspend fun getCharacters(page: Int = 1): List<CharacterUi> {
        return api.getCharacters(page).results.map { it.toCharacterUi() }
    }

    suspend fun searchByName(query: String): List<CharacterUi> {
        return api.searchCharactersByName(query).results.map { it.toCharacterUi() }
    }

    suspend fun getCharacterById(id: Int): CharacterUi {
        return api.getCharacterById(id).toCharacterUi()
    }
}
