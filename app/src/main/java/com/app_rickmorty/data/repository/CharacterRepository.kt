package com.app_rickmorty.data.repository;

import com.app_rickmorty.data.model.CharacterUi
import com.app_rickmorty.data.model.toCharacterUi
import com.app_rickmorty.data.remote.RetrofitClient
import com.app_rickmorty.data.remote.RickAndMortyApi

/** Resultado de una página del listado, con el número de la siguiente página (o null si es la última). */
data class CharacterPageResult(
    val characters: List<CharacterUi>,
    val nextPage: Int?
)

class CharacterRepository(
    private val api: RickAndMortyApi = RetrofitClient.api
) {
    /** Usado por Portal Explorer: búsqueda + filtros, primera página de resultados. */
    suspend fun getCharacters(
        page: Int = 1,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ): List<CharacterUi> {
        return api.getCharacters(
            page = page,
            name = name,
            status = status,
            species = species,
            gender = gender
        ).results.map { it.toCharacterUi() }
    }

    /** Trae una página completa sin filtrar (usada por Multiverso para el scroll infinito). */
    suspend fun getCharactersPage(page: Int): CharacterPageResult {
        val response = api.getCharacters(page = page)
        val nextPage = response.info.next?.let { page + 1 }
        return CharacterPageResult(
            characters = response.results.map { it.toCharacterUi() },
            nextPage = nextPage
        )
    }

    suspend fun getCharacterById(id: Int): CharacterUi {
        return api.getCharacterById(id).toCharacterUi()
    }
}