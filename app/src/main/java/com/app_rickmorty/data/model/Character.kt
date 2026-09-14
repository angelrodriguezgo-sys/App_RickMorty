package com.app_rickmorty.data.model

import androidx.compose.ui.graphics.Color
import com.app_rickmorty.data.remote.CharacterDto
import com.app_rickmorty.ui.screens.NeonGreen
import com.app_rickmorty.ui.screens.StatusYellow



/**
 * Modelo único de personaje usado por la tarjeta (Portal Explorer) y por el
 * detalle. Se llena con datos reales de rickandmortyapi.com a través de
 * [CharacterDto.toCharacterUi].
 */
data class CharacterUi(
    val id: Int,
    val name: String,
    val role: String,
    val idLabel: String,
    val dimension: String,
    val classification: String,
    val isAlive: Boolean,
    val species: String,
    val origin: String,
    val firstEpisode: String,
    val description: String,
    val imageUrl: String,
    val accentColor: Color = NeonGreen
)

/**
 * La API pública no trae "role" ni "classification" (eso era decorativo en el
 * diseño original), así que los derivamos de los campos reales que sí trae:
 * status, species, type y gender. Ajusta esta lógica libremente.
 */
fun CharacterDto.toCharacterUi(): CharacterUi {
    val alive = status.equals("Alive", ignoreCase = true)

    val classification = when {
        status.equals("Dead", ignoreCase = true) -> "DECEASED"
        status.equals("unknown", ignoreCase = true) -> "UNKNOWN STATUS"
        type.isNotBlank() -> type.uppercase()
        else -> "STANDARD"
    }

    val role = if (type.isNotBlank()) type else "$species • $gender"

    val episodeCount = episode.size
    val firstEpisodeId = episode.firstOrNull()
        ?.substringAfterLast("/")
        ?.toIntOrNull()

    val firstEpisodeLabel = if (firstEpisodeId != null) {
        "Episodio #$firstEpisodeId · $episodeCount apariciones en total"
    } else {
        "Sin episodios registrados"
    }

    return CharacterUi(
        id = id,
        name = name,
        role = role.uppercase(),
        idLabel = "#${id.toString().padStart(4, '0')}",
        dimension = location.name.ifBlank { origin.name },
        classification = classification,
        isAlive = alive,
        species = species,
        origin = origin.name,
        firstEpisode = firstEpisodeLabel,
        description = "Especie: $species. Género: $gender. Última ubicación conocida: ${location.name}.",
        imageUrl = image,
        accentColor = if (alive) NeonGreen else StatusYellow
    )
}
