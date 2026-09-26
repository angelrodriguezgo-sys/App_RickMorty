package com.app_rickmorty.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.app_rickmorty.ui.theme.*

@Composable
fun PortalBottomBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
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
                onClick = { onTabSelected(index) },
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