package com.example.school_companion.feature.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.school_companion.feature.auth.AuthButton
import com.example.school_companion.ui.field.EmailField
import com.example.school_companion.ui.field.NameField
import com.example.school_companion.ui.field.OrganizationField
import com.example.school_companion.ui.field.PasswordInputField
import com.example.school_companion.ui.field.SurnameField
import com.example.school_companion.ui.link.CustomLink
import com.example.school_companion.ui.message.ErrorMessage
import com.example.school_companion.ui.util.UiState

@Composable
fun RegisterCard(
    formState: RegisterFormState,
    onFormChange: (RegisterFormState) -> Unit,
    authState: UiState<Unit>,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        NameField(formState.name) { onFormChange(formState.copy(name = it)) }
        SurnameField(formState.surname) { onFormChange(formState.copy(surname = it)) }
        EmailField(formState.email) { onFormChange(formState.copy(email = it)) }

        PasswordInputField(formState, onFormChange)
        PasswordInputField(formState, onFormChange, confirm = true)

        OrganizationField(formState.organization) { onFormChange(formState.copy(organization = it)) }

        if (authState is UiState.Error) {
            ErrorMessage(authState.message)
        }

        AuthButton(
            enabled = authState !is UiState.Loading &&
                    formState.name.isNotBlank() &&
                    formState.surname.isNotBlank() &&
                    formState.email.isNotBlank() &&
                    formState.password.isNotBlank() &&
                    formState.password == formState.confirmPassword,
            loading = authState is UiState.Loading,
            text = "Registration",
            onClick = onRegisterClick
        )

        CustomLink(text = "Bereits ein Konto? Anmelden", onClick = onLoginClick)
    }
}

data class RegisterFormState(
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val organization: String = ""
)
