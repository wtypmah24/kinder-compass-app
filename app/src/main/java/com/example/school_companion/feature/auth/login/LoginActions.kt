package com.example.school_companion.feature.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.school_companion.feature.auth.AuthButton
import com.example.school_companion.ui.link.CustomLink

@Composable
fun LoginActions(
    loginEnabled: Boolean,
    loginLoading: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AuthButton(
            enabled = loginEnabled,
            loading = loginLoading,
            text = "Login",
            onClick = onLoginClick
        )
        CustomLink(text = "Noch kein Konto? Registrieren", onRegisterClick)
    }
}