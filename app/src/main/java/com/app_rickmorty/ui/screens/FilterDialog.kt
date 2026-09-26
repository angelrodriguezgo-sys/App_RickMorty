package com.app_rickmorty.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.window.Dialog
import com.app_rickmorty.ui.theme.*
import com.app_rickmorty.ui.viewmodel.CharacterFilters

@Composable
fun FilterDialog(
    currentFilters: CharacterFilters,
    onDismiss: () -> Unit,
    onApply: (CharacterFilters) -> Unit
) {
    var status by remember { mutableStateOf(currentFilters.status) }
    var gender by remember { mutableStateOf(currentFilters.gender) }
    var species by remember { mutableStateOf(currentFilters.species) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(PanelDark)
                .border(BorderStroke(1.dp, BorderSubtle), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Filtros", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("ESTADO", color = TextSecondary, fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            FilterChipsRow(
                options = listOf(
                    "Todos" to null,
                    "Vivo" to "alive",
                    "Muerto" to "dead",
                    "Desconocido" to "unknown"
                ),
                selected = status,
                onSelect = { status = it }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("GÉNERO", color = TextSecondary, fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            FilterChipsRow(
                options = listOf(
                    "Todos" to null,
                    "Masculino" to "male",
                    "Femenino" to "female",
                    "Sin género" to "genderless",
                    "Desconocido" to "unknown"
                ),
                selected = gender,
                onSelect = { gender = it }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("ESPECIE", color = TextSecondary, fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = species,
                onValueChange = { species = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ej: Human, Alien, Robot...", color = TextSecondary) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = PanelDarkAlt,
                    unfocusedContainerColor = PanelDarkAlt,
                    focusedBorderColor = BorderSubtle,
                    unfocusedBorderColor = BorderSubtle,
                    cursorColor = NeonGreen
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = {
                        status = null
                        gender = null
                        species = ""
                        onApply(CharacterFilters())
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, BorderSubtle),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
                ) {
                    Text("Limpiar")
                }
                Button(
                    onClick = {
                        onApply(CharacterFilters(status = status, gender = gender, species = species))
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = SpaceBlack)
                ) {
                    Text("Aplicar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun FilterChipsRow(
    options: List<Pair<String, String?>>,
    selected: String?,
    onSelect: (String?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { (label, value) ->
            val isSelected = selected == value
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(value) },
                label = { Text(label, fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = PanelDarkAlt,
                    labelColor = TextSecondary,
                    selectedContainerColor = NeonGreen.copy(alpha = 0.2f),
                    selectedLabelColor = NeonGreen
                )
            )
        }
    }
}