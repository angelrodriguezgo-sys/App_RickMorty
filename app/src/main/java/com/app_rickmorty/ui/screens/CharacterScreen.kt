package com.app_rickmorty.ui.screens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app_rickmorty.data.model.CharacterUi
import com.app_rickmorty.ui.theme.*

@Composable
fun CharacterDetailScreen(
    character: CharacterUi,
    onBack: () -> Unit,
    onAddToFavorites: () -> Unit,
    selectedTab: Int = 0
) {
    Scaffold(containerColor = SpaceBlack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SpaceBlack)
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Top bar: back + badge de dimensión
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(PanelDark),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = TextPrimary)
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(PanelDark)
                        .border(BorderStroke(1.dp, NeonGreen.copy(alpha = 0.6f)), RoundedCornerShape(50))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = character.dimension,
                        color = NeonGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Retrato circular con glow
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .background(PortalGlowBrush, CircleShape)
                        .border(BorderStroke(3.dp, NeonGreen), CircleShape)
                        .padding(6.dp)
                        .clip(CircleShape)
                        .background(PanelDarkAlt)
                ) {
                    AsyncImage(
                        model = character.imageUrl,
                        contentDescription = character.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = character.name.uppercase(),
                color = NeonGreen,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "${character.role} • ID: ${character.idLabel}",
                color = PurpleAccent,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjeta de información con borde ámbar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(PanelDark)
                    .border(BorderStroke(1.dp, DangerOrange.copy(alpha = 0.5f)), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Warning,
                        contentDescription = null,
                        tint = DangerOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CLASIFICACIÓN: ${character.classification}",
                        color = DangerOrange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                InfoRow(label = "STATUS") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (character.isAlive) StatusGreen else Color.Red)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (character.isAlive) "ALIVE" else "DECEASED",
                            color = StatusGreen,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                HorizontalDivider(color = BorderSubtle, thickness = 1.dp)

                InfoRow(label = "SPECIES") {
                    Text(character.species, color = PurpleAccent, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                HorizontalDivider(color = BorderSubtle, thickness = 1.dp)

                InfoRow(label = "ORIGIN") {
                    Text(character.origin, color = PurpleAccent, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                HorizontalDivider(color = BorderSubtle, thickness = 1.dp)

                InfoRow(label = "FIRST EPISODE") {
                    Text(character.firstEpisode, color = PurpleAccent, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = character.description,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = onAddToFavorites,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                border = BorderStroke(1.dp, PurpleAccent),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = PurpleAccent)
            ) {
                Icon(Icons.Filled.Favorite, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "ADD TO FAVORITES", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InfoRow(label: String, value: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = TextSecondary, fontSize = 12.sp, letterSpacing = 0.5.sp)
        value()
    }
}