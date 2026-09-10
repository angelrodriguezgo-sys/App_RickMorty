package com.app_rickmorty.data.remote

interface RickAndMortyApi  {
    @GET("character")
    suspend fun getCharacter(
        @Query("name") name : String?
    ): CharacterResponse


    @GET("character/{id}")
    suspend fun getCharacterByid(
        @path("id") id: Int
    ): RickCharacter
}

object RetrofitClient {

    private const val BASE_URL = "https://rickandmortyapi.com/api"

    val api : RickAndMortyApi by lazy {
        Retrofit.Builder
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApi::class.java)
    }
}