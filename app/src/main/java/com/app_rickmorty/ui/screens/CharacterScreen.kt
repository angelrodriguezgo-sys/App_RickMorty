package com.app_rickmorty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app_rickmorty.data.model.RickCharacter
import com.app_rickmorty.ui.theme.DangerAmber
import com.app_rickmorty.ui.theme.MultiverseLavender
import com.app_rickmorty.ui.theme.PortalBlack
import com.app_rickmorty.ui.theme.PortalGreen
import com.app_rickmorty.ui.theme.TextSecondary




@Composable
fun CharacterScreen(
    characters: List<RickCharacter>,
    onCharacterClick: (RickCharacter) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PortalBlack)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Portal Explorer",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(characters) { character ->
                CharacterCard(
                    character = character,
                    onClick = { onCharacterClick(character) }
                )
            }
        }
    }
}

// ---------- CARD DE PERSONAJE ----------
@Composable
fun CharacterCard(
    character: RickCharacter,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column {

            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(12.dp)) {

                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = character.species.uppercase(),
                    style = MaterialTheme.typography.labelMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                // La API de Rick and Morty devuelve status en inglés: "Alive", "Dead", "unknown"
                val statusColor = when (character.status) {
                    "Alive" -> PortalGreen
                    "Dead" -> DangerAmber
                    else -> TextSecondary
                }
                val statusText = when (character.status) {
                    "Alive" -> "Vivo"
                    "Dead" -> "Muerto"
                    else -> "Desconocido"
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelLarge,
                        color = statusColor
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "ORIGEN:", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = character.origin.name,
                    style = MaterialTheme.typography.bodySmall.copy(color = MultiverseLavender),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun AsyncImage(
    model: Int,
    contentDescription: String,
    modifier: Modifier,
    contentScale: ContentScale
) {
    TODO("Not yet implemented")
}