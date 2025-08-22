package com.example.school_companion.ui.card.auth

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
import com.example.school_companion.ui.button.auth.RegisterButton
import com.example.school_companion.ui.field.auth.EmailField
import com.example.school_companion.ui.field.auth.PasswordField
import com.example.school_companion.ui.link.LoginLink
import com.example.school_companion.ui.message.ErrorMessage
import com.example.school_companion.ui.viewmodel.AuthState

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
    authState: AuthState,
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

            if (authState is AuthState.Error) {
                ErrorMessage((authState).message)
            }

            RegisterButton(
                enabled = authState !is AuthState.Loading &&
                        name.isNotBlank() && surname.isNotBlank() &&
                        email.isNotBlank() && password.isNotBlank() &&
                        password == confirmPassword,
                loading = authState is AuthState.Loading,
                onClick = onRegisterClick
            )

            LoginLink(onLoginClick)
        }
    }
}