package com.example.school_companion.ui.field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.school_companion.feature.auth.register.RegisterFormState

@Composable
fun PasswordInputField(
    formState: RegisterFormState,
    onFormChange: (RegisterFormState) -> Unit,
    confirm: Boolean = false
) {
    val value = if (confirm) formState.confirmPassword else formState.password
    val visible = if (confirm) formState.confirmPasswordVisible else formState.passwordVisible
    val label = if (confirm) "Passwort bestätigen" else "Passwort"

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (confirm) onFormChange(formState.copy(confirmPassword = newValue))
            else onFormChange(formState.copy(password = newValue))
        },
        label = { Text(label) },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = label) },
        trailingIcon = {
            IconButton(
                onClick = {
                    if (confirm) onFormChange(formState.copy(confirmPasswordVisible = !visible))
                    else onFormChange(formState.copy(passwordVisible = !visible))
                }
            ) {
                Icon(
                    imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (visible) "Hide password" else "Show password"
                )
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        isError = confirm && formState.password != formState.confirmPassword && value.isNotEmpty()
    )
}
