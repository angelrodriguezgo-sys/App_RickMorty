package com.app_rickmorty.data.model

data class CharacterResponse(
    val results: List<RickCharacter>
)

data class RickCharacter(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val image : Int,
    val origin: String
)

data class Origin(
    val name: String
)