package com.app_rickmorty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_rickmorty.data.model.CharacterUi
import com.app_rickmorty.ui.viewmodel.CharactersUiState
import com.app_rickmorty.ui.theme.*

@Composable
fun PortalExplorerScreen(
    uiState: CharactersUiState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    onFiltrosClick: () -> Unit,
    activeFilterCount: Int = 0,
    favoriteIds: Set<Int> = emptySet(),
    onToggleFavorite: (CharacterUi) -> Unit = {},
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit = {},
    onCharacterClick: (CharacterUi) -> Unit = {}
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

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(PanelDark)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "CITADEL DATABASE", color = NeonGreen, fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "Portal Explorer", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(SpaceBlack)
                        .border(1.dp, NeonGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Outlined.Public, contentDescription = "Multiverso", tint = NeonGreen, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Barra de búsqueda -> dispara una llamada real a /character?name=
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar personaje...", color = TextSecondary) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = TextSecondary) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Search
                ),
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onSearch = { onSearchSubmit() }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = PanelDark,
                    unfocusedContainerColor = PanelDark,
                    focusedBorderColor = BorderSubtle,
                    unfocusedBorderColor = BorderSubtle,
                    cursorColor = NeonGreen
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botón Filtros (con contador cuando hay filtros activos)
            OutlinedButton(
                onClick = onFiltrosClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (activeFilterCount > 0) NeonGreen else BorderSubtle
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = PanelDark,
                    contentColor = NeonGreen
                )
            ) {
                Icon(Icons.Filled.Tune, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (activeFilterCount > 0) "Filtros ($activeFilterCount)" else "Filtros",
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (uiState) {
                is CharactersUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth().height(180.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = NeonGreen)
                    }
                }

                is CharactersUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().height(180.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "No se encontraron resultados", color = Color.Red, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                        Text(text = uiState.message, color = TextSecondary, fontSize = 12.sp, textAlign = TextAlign.Center)
                    }
                }

                is CharactersUiState.Success -> {
                    val characters = uiState.characters

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Anomalías Detectadas", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "${characters.size} resultados", color = NeonGreen, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (characters.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().height(140.dp), contentAlignment = Alignment.Center) {
                            Text(
                                "No hay personajes que coincidan con la búsqueda/filtros",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(characters) { character ->
                                CharacterCard(
                                    character = character,
                                    isFavorite = favoriteIds.contains(character.id),
                                    onClick = { onCharacterClick(character) },
                                    onToggleFavorite = { onToggleFavorite(character) }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "CITADEL STATUS NETWORK", color = TextSecondary, fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(PanelDark)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isOnline = uiState !is CharactersUiState.Error
                StatusDot(color = if (isOnline) StatusGreen else Color.Red)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isOnline) "Conectado a rickandmortyapi.com" else "Sin conexión con la API",
                    color = TextPrimary,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}