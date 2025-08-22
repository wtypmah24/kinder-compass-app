package com.example.school_companion.ui.card.profile

import android.net.Uri
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.school_companion.data.model.Companion
import com.example.school_companion.data.repository.SessionManager.token
import com.example.school_companion.ui.dialog.companion.EditCompanionDialog
import com.example.school_companion.ui.dialog.companion.UpdatePasswordDialog
import com.example.school_companion.ui.viewmodel.AuthViewModel
import com.example.school_companion.ui.viewmodel.CompanionViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserInfoCard(
    currentUser: Companion?,
    pickImageLauncher: ManagedActivityResultLauncher<String, Uri?>,
    companionViewModel: CompanionViewModel,
    token: String,
    authViewModel: AuthViewModel
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showUpdatePasswordDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Benutzerinformationen",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                currentUser?.let { user ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AvatarCard(
                            avatarId = user.avatarId ?: "",
                            onChangeAvatarClick = { pickImageLauncher.launch("image/*") }
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                        ) {
                            Text(
                                text = "${user.name} ${user.surname}",
                                fontSize = 20.sp,
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = user.email,
                                fontSize = 14.sp,
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Organization: ${user.organization}",
                                fontSize = 14.sp,
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Start working day: ${user.startWorkingTime}",
                                fontSize = 14.sp,
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "End working day: ${user.endWorkingTime}",
                                fontSize = 14.sp,
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Buttons row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { showEditDialog = true }) {
                        Text("Update Info")
                    }
                    Button(onClick = { showUpdatePasswordDialog = true }) {
                        Text("Update Password")
                    }
                    Button(
                        onClick = { showDeleteConfirm = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Delete", color = Color.White)
                    }
                }
            }
        }
    }
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete account?") },
            text = {
                if (currentUser != null) {
                    Text("Are you sure you want to delete «${currentUser.name}: ${currentUser.surname}»?")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        token.let { companionViewModel.deleteCompanion(it) { authViewModel.logout() } }
                        showDeleteConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    if (showEditDialog) {
        if (currentUser != null) {
            EditCompanionDialog(
                companion = currentUser,
                onDismiss = { showEditDialog = false }
            ) { dto ->
                companionViewModel.updateCompanion(
                    token,
                    dto
                ) { authViewModel.getUserProfile() }
            }
        }
    }
    if (showUpdatePasswordDialog) {
        UpdatePasswordDialog(
            onDismiss = { showUpdatePasswordDialog = false },
            onSave = { dto ->
                companionViewModel.updatePassword(token, dto)
            })
    }

}