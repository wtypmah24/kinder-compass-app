package com.example.school_companion.ui.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.school_companion.ui.button.auth.LoginButton
import com.example.school_companion.ui.link.RegisterLink

@Composable
fun LoginActions(
    loginEnabled: Boolean,
    loginLoading: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        LoginButton(enabled = loginEnabled, loading = loginLoading, onClick = onLoginClick)
        RegisterLink(onRegisterClick)
    }
}