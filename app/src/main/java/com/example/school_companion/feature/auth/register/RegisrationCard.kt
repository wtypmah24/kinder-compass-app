package com.example.school_companion.feature.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.school_companion.feature.auth.AuthButton
import com.example.school_companion.ui.field.ConfirmPasswordField
import com.example.school_companion.ui.field.EmailField
import com.example.school_companion.ui.field.NameField
import com.example.school_companion.ui.field.OrganizationField
import com.example.school_companion.ui.field.PasswordField
import com.example.school_companion.ui.field.SurnameField
import com.example.school_companion.ui.link.CustomLink
import com.example.school_companion.ui.message.ErrorMessage
import com.example.school_companion.ui.util.UiState

@Composable
fun RegisterCard(
    name: String,
    onNameChange: (String) -> Unit,
    surname: String,
    onSurnameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibilityChange: (Boolean) -> Unit,
    organization: String,
    onOrganizationChange: (String) -> Unit,
    authState: UiState<Unit>,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Neues Konto erstellen",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            NameField(name, onNameChange)
            SurnameField(surname, onSurnameChange)
            EmailField(email, onEmailChange)
            PasswordField(password, onPasswordChange, passwordVisible, onPasswordVisibilityChange)
            ConfirmPasswordField(
                confirmPassword,
                onConfirmPasswordChange,
                confirmPasswordVisible,
                onConfirmPasswordVisibilityChange,
                password
            )
            OrganizationField(organization, onOrganizationChange)

            if (authState is UiState.Error) {
                ErrorMessage((authState).message)
            }

            AuthButton(
                enabled = authState !is UiState.Loading &&
                        name.isNotBlank() && surname.isNotBlank() &&
                        email.isNotBlank() && password.isNotBlank() &&
                        password == confirmPassword,
                loading = authState is UiState.Loading,
                text = "Registration",
                onClick = onRegisterClick
            )

            CustomLink(text = "Bereits ein Konto? Anmelden", onLoginClick)
        }
    }
}