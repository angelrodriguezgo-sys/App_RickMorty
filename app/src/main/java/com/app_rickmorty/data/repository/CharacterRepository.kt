package com.app_rickmorty.data.repository;


import com.app_rickmorty.data.model.RickCharacter;
import com.app_rickmorty.data.remote.RetrofitClient;



public class CharacterRepository {
    private val api = RetrofitClient.api

    suspend fun getCharacters(name: String? = null): Result<List<RickCharacter>> {
        return try {
            val response = api.getCharacter(name = name)
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    };
}
