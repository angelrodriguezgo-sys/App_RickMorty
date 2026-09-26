package com.app_rickmorty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_rickmorty.data.model.CharacterUi
import com.app_rickmorty.ui.theme.*
import com.app_rickmorty.ui.viewmodel.CharactersUiState

@Composable
fun FavoritesScreen(
    state: CharactersUiState,
    onCharacterClick: (CharacterUi) -> Unit,
    onToggleFavorite: (CharacterUi) -> Unit,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Scaffold(
        containerColor = SpaceBlack,
        bottomBar = { PortalBottomBar(selectedTab = selectedTab, onTabSelected = onTabSelected) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SpaceBlack)
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("TUS ANOMALÍAS", color = NeonGreen, fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
            Text("Favoritos", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            when (state) {
                is CharactersUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = NeonGreen)
                    }
                }

                is CharactersUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("No se pudieron cargar tus favoritos", color = TextPrimary, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                        Text(state.message, color = TextSecondary, fontSize = 12.sp, textAlign = TextAlign.Center)
                    }
                }

                is CharactersUiState.Success -> {
                    if (state.characters.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Outlined.BookmarkBorder, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Todavía no tienes favoritos", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Toca el corazón en cualquier tarjeta para guardarla aquí.",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 24.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(state.characters, key = { it.id }) { character ->
                                CharacterCard(
                                    character = character,
                                    isFavorite = true,
                                    onClick = { onCharacterClick(character) },
                                    onToggleFavorite = { onToggleFavorite(character) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}