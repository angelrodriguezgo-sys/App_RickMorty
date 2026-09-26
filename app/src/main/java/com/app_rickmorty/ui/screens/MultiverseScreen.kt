package com.app_rickmorty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_rickmorty.data.model.CharacterUi
import com.app_rickmorty.ui.theme.*
import com.app_rickmorty.ui.viewmodel.MultiverseUiState

@Composable
fun MultiverseScreen(
    state: MultiverseUiState,
    favoriteIds: Set<Int>,
    onLoadMore: () -> Unit,
    onCharacterClick: (CharacterUi) -> Unit,
    onToggleFavorite: (CharacterUi) -> Unit,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val gridState = rememberLazyGridState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisible >= totalItems - 6
        }
    }

    LaunchedEffect(shouldLoadMore, state.isLoading, state.isLoadingMore, state.endReached) {
        if (shouldLoadMore && !state.isLoading && !state.isLoadingMore && !state.endReached) {
            onLoadMore()
        }
    }

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
            Text("MULTIVERSO", color = NeonGreen, fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
            Text("Todos los personajes", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = "${state.characters.size} personajes cargados", color = TextSecondary, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(16.dp))

            when {
                state.isLoading && state.characters.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = NeonGreen)
                    }
                }

                state.error != null && state.characters.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("No se pudo cargar el multiverso", color = Color.Red, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(state.error, color = TextSecondary, fontSize = 12.sp, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(onClick = onLoadMore, shape = RoundedCornerShape(20.dp)) {
                            Text("Reintentar")
                        }
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = gridState,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 24.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(state.characters, key = { it.id }) { character ->
                            CharacterCard(
                                character = character,
                                isFavorite = favoriteIds.contains(character.id),
                                onClick = { onCharacterClick(character) },
                                onToggleFavorite = { onToggleFavorite(character) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        if (state.isLoadingMore) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = NeonGreen, modifier = Modifier.size(28.dp))
                                }
                            }
                        } else if (state.error != null) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("No se pudieron cargar más personajes", color = TextSecondary, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedButton(onClick = onLoadMore, shape = RoundedCornerShape(20.dp)) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                        } else if (state.endReached) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Text(
                                    "Has llegado al final del multiverso conocido",
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}