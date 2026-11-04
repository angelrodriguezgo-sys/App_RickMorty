package com.app_rickmorty.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.app_rickmorty.data.local.ImageUtils
import com.app_rickmorty.ui.theme.*
import com.app_rickmorty.ui.viewmodel.ProfileUiState
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onSaveUsername: (String) -> Unit,
    onRegenerate: () -> Unit,
    onPhotoSelected: (String) -> Unit,
    onRemovePhoto: () -> Unit,
    onDismissError: () -> Unit,
    onLogout: () -> Unit,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    var usernameField by remember(state.username) { mutableStateOf(state.username) }
    var isEditingUsername by remember { mutableStateOf(false) }
    var showPhotoOptionsDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.username, state.isSavingUsername) {
        if (!state.isSavingUsername) {
            usernameField = state.username
            isEditingUsername = false
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                val base64 = ImageUtils.uriToBase64(context, uri)
                if (base64 != null) onPhotoSelected(base64)
            }
        }
    }

    // Diálogo de opciones cuando el usuario ya tiene foto (Cambiar o Eliminar)
    if (showPhotoOptionsDialog) {
        Dialog(onDismissRequest = { showPhotoOptionsDialog = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(PanelDark)
                    .border(BorderStroke(1.dp, BorderSubtle), RoundedCornerShape(20.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Foto de Perfil",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        showPhotoOptionsDialog = false
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = SpaceBlack)
                ) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cambiar foto", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = {
                        showPhotoOptionsDialog = false
                        onRemovePhoto()
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Eliminar foto", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                TextButton(
                    onClick = { showPhotoOptionsDialog = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar", color = TextSecondary)
                }
            }
        }
    }

    Scaffold(
        containerColor = SpaceBlack,
        bottomBar = { PortalBottomBar(selectedTab = selectedTab, onTabSelected = onTabSelected) }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpaceBlack)
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = NeonGreen)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SpaceBlack)
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("CITADEL ID", color = NeonGreen, fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
            Text("Perfil", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            if (state.errorMessage != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Red.copy(alpha = 0.12f))
                        .border(BorderStroke(1.dp, Color.Red.copy(alpha = 0.4f)), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.errorMessage,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismissError, modifier = Modifier.size(22.dp)) {
                        Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = Color.Red)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Avatar: Si ya hay foto abre el selector (Editar/Eliminar), si no hay abre la galería directamente
            val onAvatarClick = {
                if (state.photoBase64.isNotBlank()) {
                    showPhotoOptionsDialog = true
                } else {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            }

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(PortalGlowBrush, CircleShape)
                        .border(BorderStroke(2.dp, NeonGreen), CircleShape)
                        .clickable { onAvatarClick() },
                    contentAlignment = Alignment.Center
                ) {
                    val bitmap = remember(state.photoBase64) {
                        if (state.photoBase64.isNotBlank()) {
                            ImageUtils.base64ToBitmap(state.photoBase64)
                        } else null
                    }

                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    } else {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(44.dp))
                    }

                    if (state.isUploadingPhoto) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.55f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = NeonGreen, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        }
                    }

                    // Único botón flotante de camarita elegante en la esquina inferior derecha
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(NeonGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.CameraAlt,
                            contentDescription = "Foto de perfil",
                            tint = SpaceBlack,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (state.photoBase64.isNotBlank())
                    "Toca la foto para modificarla o eliminarla"
                else
                    "Toca la camarita para agregar una foto",
                color = TextSecondary,
                fontSize = 11.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            if (state.email.isNotBlank()) {
                Text(
                    text = state.email,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjeta de Nombre de Usuario (Alias) con botón elegante de edición
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(PanelDark)
                    .border(BorderStroke(1.dp, BorderSubtle), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                if (!isEditingUsername) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("NOMBRE DE USUARIO (ALIAS)", color = TextSecondary, fontSize = 11.sp, letterSpacing = 0.5.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = state.username.ifBlank { "Sin alias" },
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Botón pequeño elegante para editar el nombre
                        IconButton(
                            onClick = { isEditingUsername = true },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PanelDarkAlt)
                                .border(BorderStroke(1.dp, BorderSubtle), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Editar nombre",
                                tint = NeonGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                } else {
                    Text("EDITAR NOMBRE DE USUARIO", color = NeonGreen, fontSize = 11.sp, letterSpacing = 0.5.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = usernameField,
                        onValueChange = { usernameField = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ej: RickC137", color = TextSecondary) },
                        singleLine = true,
                        enabled = !state.isSavingUsername,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = PanelDarkAlt,
                            unfocusedContainerColor = PanelDarkAlt,
                            disabledContainerColor = PanelDarkAlt,
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = BorderSubtle,
                            cursorColor = NeonGreen
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                usernameField = state.username
                                isEditingUsername = false
                            },
                            modifier = Modifier.weight(1f).height(44.dp),
                            enabled = !state.isSavingUsername,
                            shape = RoundedCornerShape(22.dp),
                            border = BorderStroke(1.dp, BorderSubtle),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
                        ) {
                            Text("Cancelar", fontSize = 13.sp)
                        }

                        Button(
                            onClick = { onSaveUsername(usernameField) },
                            modifier = Modifier.weight(1f).height(44.dp),
                            enabled = usernameField.isNotBlank() && !state.isSavingUsername,
                            shape = RoundedCornerShape(22.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = SpaceBlack)
                        ) {
                            if (state.isSavingUsername) {
                                CircularProgressIndicator(color = SpaceBlack, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Guardar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
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
                    enabled = !state.isSavingCoordinates,
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, PurpleAccent),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PurpleAccent)
                ) {
                    if (state.isSavingCoordinates) {
                        CircularProgressIndicator(color = PurpleAccent, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("GENERANDO...", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    } else {
                        Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("GENERAR NUEVAS COORDENADAS", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.6f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Icon(Icons.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("CERRAR SESIÓN", fontWeight = FontWeight.Bold, fontSize = 13.sp)
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