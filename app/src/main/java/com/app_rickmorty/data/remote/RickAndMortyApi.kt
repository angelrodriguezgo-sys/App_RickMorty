package com.app_rickmorty.data.remote


import androidx.contentpager.content.Query
import com.app_rickmorty.data.model.CharacterResponse;
import com.app_rickmorty.data.model.RickCharacter;
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path



interface RickAndMortyApi  {
    @GET("character")
    suspend fun getCharacter(
        @retrofit2.http.Query("name") name : String?
    ): CharacterResponse


    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): RickCharacter
}



object RetrofitClient {
    private const val BASE_URL = "https://rickandmortyapi.com/api"

    val api : RickAndMortyApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApi::class.java)
    }
}


