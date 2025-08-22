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
import com.example.school_companion.ui.button.auth.LoginButton
import com.example.school_companion.ui.field.auth.EmailField
import com.example.school_companion.ui.field.auth.PasswordField
import com.example.school_companion.ui.link.RegisterLink
import com.example.school_companion.ui.message.ErrorMessage
import com.example.school_companion.ui.viewmodel.AuthState

@Composable
fun LoginCard(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    authState: AuthState,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
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
                text = "Anmelden",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            EmailField(email, onEmailChange)
            PasswordField(password, onPasswordChange, passwordVisible, onPasswordVisibilityChange)

            if (authState is AuthState.Error) {
                ErrorMessage(authState.message)
            }

            LoginButton(
                enabled = authState !is AuthState.Loading && email.isNotBlank() && password.isNotBlank(),
                loading = authState is AuthState.Loading,
                onClick = onLoginClick
            )

            RegisterLink(onRegisterClick)
        }
    }
}