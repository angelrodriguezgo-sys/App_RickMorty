package com.app_rickmorty.data.repository;

public class CharacterRepository {
    private val api = RetrofitClient.api

    suspend fun getCharacter(name: String? = null): Result<List<RickCharacter>>{
        return try {
            val response = api.getCharacters(name = name)
            Result.success(response.results)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}
