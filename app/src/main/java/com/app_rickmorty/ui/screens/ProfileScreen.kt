package com.app_rickmorty.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app_rickmorty.ui.theme.*
import com.app_rickmorty.ui.viewmodel.ProfileUiState

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onSaveUsername: (String) -> Unit,
    onRegenerate: () -> Unit,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    var usernameField by remember(state.username) { mutableStateOf(state.username) }
    var justSaved by remember { mutableStateOf(false) }

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
            Text("CITADEL ID", color = NeonGreen, fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
            Text("Perfil", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(PortalGlowBrush, CircleShape)
                        .border(BorderStroke(2.dp, NeonGreen), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Person, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(44.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(PanelDark)
                    .border(BorderStroke(1.dp, BorderSubtle), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text("NOMBRE DE USUARIO", color = TextSecondary, fontSize = 11.sp, letterSpacing = 0.5.sp)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = usernameField,
                    onValueChange = {
                        usernameField = it
                        justSaved = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ej: RickC137", color = TextSecondary) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = PanelDarkAlt,
                        unfocusedContainerColor = PanelDarkAlt,
                        focusedBorderColor = NeonGreen,
                        unfocusedBorderColor = BorderSubtle,
                        cursorColor = NeonGreen
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        onSaveUsername(usernameField.trim())
                        justSaved = true
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    enabled = usernameField.isNotBlank(),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = SpaceBlack)
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (justSaved) "GUARDADO ✓" else "GUARDAR NOMBRE", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(PanelDark)
                    .border(BorderStroke(1.dp, PurpleAccent.copy(alpha = 0.5f)), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text("COORDENADAS DIMENSIONALES", color = PurpleAccent, fontSize = 11.sp, letterSpacing = 0.5.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(12.dp))

                ProfileDataRow(label = "DIMENSIÓN", value = state.dimension)
                Spacer(modifier = Modifier.height(10.dp))
                ProfileDataRow(label = "TIERRA", value = state.tierra)

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onRegenerate,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, PurpleAccent),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PurpleAccent)
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("GENERAR NUEVAS COORDENADAS", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ProfileDataRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TextSecondary, fontSize = 12.sp, letterSpacing = 0.5.sp)
        Text(value, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}