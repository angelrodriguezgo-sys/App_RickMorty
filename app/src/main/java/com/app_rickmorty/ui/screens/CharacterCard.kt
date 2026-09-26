package com.app_rickmorty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_rickmorty.data.model.CharacterUi
import com.app_rickmorty.ui.theme.*

@Composable
fun CharacterCard(
    character: CharacterUi,
    isFavorite: Boolean,
    onClick: () -> Unit = {},
    onToggleFavorite: () -> Unit = {},
    modifier: Modifier = Modifier.width(170.dp)
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(PanelDark)
            .clickable { onClick() }
    ) {
        Box {
            AsyncImage(
                model = character.imageUrl,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(PanelDarkAlt)
            )

            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(SpaceBlack.copy(alpha = 0.55f))
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                    tint = if (isFavorite) PurpleAccent else Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = character.name, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = character.species, color = TextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                StatusDot(color = if (character.isAlive) StatusGreen else Color.Red)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (character.isAlive) "Vivo" else "Muerto",
                    color = StatusGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "PRIMERA APARICIÓN:", color = TextSecondary, fontSize = 9.sp, letterSpacing = 0.5.sp)
            Text(
                text = character.firstEpisode,
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2
            )
        }
    }
}

/** Indicador de estado (punto de color). */
@Composable
fun StatusDot(color: Color) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color)
    )
}