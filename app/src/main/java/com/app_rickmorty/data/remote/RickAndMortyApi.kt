package com.app_rickmorty.data.remote


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// ---------------------------------------------------------------------
// DTOs: representan EXACTAMENTE el JSON que devuelve rickandmortyapi.com
// ---------------------------------------------------------------------

data class CharacterListResponseDto(
    val info: InfoDto,
    val results: List<CharacterDto>
)

data class InfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: NamedResourceDto,
    val location: NamedResourceDto,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)

data class NamedResourceDto(
    val name: String,
    val url: String
)

// ---------------------------------------------------------------------
// Retrofit
// ---------------------------------------------------------------------

interface RickAndMortyApi {

    // https://rickandmortyapi.com/api/character?page=1&name=...&status=...&species=...&gender=...
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int = 1,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("gender") gender: String? = null
    ): CharacterListResponseDto

    // https://rickandmortyapi.com/api/character/1
    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): CharacterDto
}

object RetrofitClient {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    val api: RickAndMortyApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RickAndMortyApi::class.java)
    }
}