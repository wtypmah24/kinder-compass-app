package com.example.school_companion.feature.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.feature.auth.AuthViewModel
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.navigation.Screen
import com.example.school_companion.ui.util.UiState

@Composable
fun LoginScreen(
    onNavigate: NavigateToWithArgs,
    viewModel: AuthViewModel
) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        if (authState is UiState.Success) {
            onNavigate(Screen.Dashboard, null)
        }
    }

    val loginEnabled =
        email.isNotBlank() && password.isNotBlank() && authState !is UiState.Loading
    val loginLoading = authState is UiState.Loading
    val errorMessage = (authState as? UiState.Error)?.message

    LoginContent(
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        passwordVisible = passwordVisible,
        onPasswordVisibilityChange = { passwordVisible = it },
        loginEnabled = loginEnabled,
        loginLoading = loginLoading,
        errorMessage = errorMessage,
        onLoginClick = { viewModel.login(email, password) },
        onRegisterClick = { onNavigate(Screen.Register, null) }
    )
}
