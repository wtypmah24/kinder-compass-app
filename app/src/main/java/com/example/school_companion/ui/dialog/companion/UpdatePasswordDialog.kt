package com.example.school_companion.ui.dialog.companion

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.api.PasswordUpdateDto
import com.example.school_companion.data.model.Companion

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpdatePasswordDialog(
    onDismiss: () -> Unit,
    onSave: (PasswordUpdateDto) -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var currentPasswordRepeat by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    var showCurrentPassword by remember { mutableStateOf(false) }
    var showCurrentPasswordRepeat by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Password") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Current password") },
                    visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image =
                            if (showCurrentPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                            Icon(imageVector = image, contentDescription = null)
                        }
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = currentPasswordRepeat,
                    onValueChange = { currentPasswordRepeat = it },
                    label = { Text("Repeat current password") },
                    visualTransformation = if (showCurrentPasswordRepeat) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image =
                            if (showCurrentPasswordRepeat) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = {
                            showCurrentPasswordRepeat = !showCurrentPasswordRepeat
                        }) {
                            Icon(imageVector = image, contentDescription = null)
                        }
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New password") },
                    visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image =
                            if (showNewPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { showNewPassword = !showNewPassword }) {
                            Icon(imageVector = image, contentDescription = null)
                        }
                    },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        PasswordUpdateDto(
                            currentPassword,
                            newPassword
                        )
                    )
                },
                enabled = currentPassword.isNotBlank() &&
                        currentPassword == currentPasswordRepeat &&
                        newPassword.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
