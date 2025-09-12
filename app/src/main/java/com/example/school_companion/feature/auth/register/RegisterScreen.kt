package com.example.school_companion.feature.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.feature.auth.AuthViewModel
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.navigation.Screen
import com.example.school_companion.ui.util.UiState

@Composable
fun RegisterScreen(
    onNavigate: NavigateToWithArgs,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var organization by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        if (authState is UiState.Success) {
            onNavigate(Screen.Dashboard, null)
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RegisterHeader()

            RegisterCard(
                name = name,
                onNameChange = { name = it },
                surname = surname,
                onSurnameChange = { surname = it },
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                confirmPassword = confirmPassword,
                onConfirmPasswordChange = { confirmPassword = it },
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { passwordVisible = it },
                confirmPasswordVisible = confirmPasswordVisible,
                onConfirmPasswordVisibilityChange = { confirmPasswordVisible = it },
                organization = organization,
                onOrganizationChange = { organization = it },
                authState = authState,
                onRegisterClick = {
                    viewModel.register(email, password, name, surname, organization)
                },
                onLoginClick = {
                    onNavigate(Screen.Login, null)
                }
            )
        }
    }
}
