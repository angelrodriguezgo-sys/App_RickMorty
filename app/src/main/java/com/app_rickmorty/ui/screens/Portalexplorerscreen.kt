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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_rickmorty.ui.theme.*

data class Character(
    val name: String,
    val species: String,
    val isAlive: Boolean,
    val firstAppearance: String,
    val accentColor: Color = NeonGreen
)

private val sampleCharacters = listOf(
    Character("Rick Sanchez", "HUMANO", true, "S01E01 - Pilot", NeonGreen),
    Character("Evil Morty", "HUMANO", true, "S01E10 - Close...", StatusYellow)
)

@Composable
fun PortalExplorerScreen(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onFiltrosClick: () -> Unit,
    characters: List<Character> = sampleCharacters,
    totalAnomalias: Int = 482,
    selectedTab: Int = 0
) {
    Scaffold(
        containerColor = SpaceBlack,
        bottomBar = { PortalBottomBar(selectedTab = selectedTab) }
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
                    Text(
                        text = "CITADEL DATABASE",
                        color = NeonGreen,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Portal Explorer",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(SpaceBlack)
                        .border(1.dp, NeonGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Public,
                        contentDescription = "Multiverso",
                        tint = NeonGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Barra de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar personaje...", color = TextSecondary) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = TextSecondary) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = PanelDark,
                    unfocusedContainerColor = PanelDark,
                    focusedBorderColor = BorderSubtle,
                    unfocusedBorderColor = BorderSubtle,
                    cursorColor = NeonGreen
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botón Filtros
            OutlinedButton(
                onClick = onFiltrosClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = PanelDark,
                    contentColor = NeonGreen
                )
            ) {
                Icon(Icons.Filled.Tune, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Filtros", fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sección "Anomalías Detectadas"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Anomalías Detectadas",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Ver todo ($totalAnomalias)",
                    color = NeonGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(characters) { character ->
                    CharacterCard(character = character)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Citadel status network
            Text(
                text = "CITADEL STATUS NETWORK",
                color = TextSecondary,
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(PanelDark)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusDot(color = StatusGreen)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Frecuencia central estable: C-137",
                    color = TextPrimary,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CharacterCard(character: Character) {
    Column(
        modifier = Modifier
            .width(170.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(PanelDark)
    ) {
        // Imagen del personaje (placeholder de color)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(PanelDarkAlt)
        )

        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = character.name,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = character.species,
                color = TextSecondary,
                fontSize = 11.sp
            )
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
            Text(
                text = "PRIMERA APARICIÓN:",
                color = TextSecondary,
                fontSize = 9.sp,
                letterSpacing = 0.5.sp
            )
            Text(
                text = character.firstAppearance,
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/** Indicador de estado — mismo patrón del snippet: Box + size + CircleShape + background. */
@Composable
private fun StatusDot(color: Color) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
private fun PortalBottomBar(selectedTab: Int) {
    NavigationBar(containerColor = PanelDark) {
        val items = listOf(
            Triple("Portal", Icons.Filled.Home, Icons.Outlined.Home),
            Triple("Multiverso", Icons.Filled.Public, Icons.Outlined.Public),
            Triple("Favoritos", Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder),
            Triple("Perfil", Icons.Filled.Person, Icons.Outlined.Person)
        )
        items.forEachIndexed { index, (label, filledIcon, outlinedIcon) ->
            val selected = index == selectedTab
            NavigationBarItem(
                selected = selected,
                onClick = { /* TODO: manejar navegación */ },
                icon = {
                    Icon(
                        imageVector = if (selected) filledIcon else outlinedIcon,
                        contentDescription = label
                    )
                },
                label = { Text(label, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonGreen,
                    selectedTextColor = NeonGreen,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = SpaceBlack
                )
            )
        }
    }
}