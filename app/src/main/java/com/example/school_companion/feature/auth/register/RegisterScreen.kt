package com.example.school_companion.feature.auth.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var formState by remember { mutableStateOf(RegisterFormState()) }
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        if (authState is UiState.Success) {
            onNavigate(Screen.Dashboard, null)
        }
    }

    RegisterContent(
        formState = formState,
        onFormChange = { formState = it },
        authState = authState,
        onRegisterClick = {
            viewModel.register(
                formState.email,
                formState.password,
                formState.name,
                formState.surname,
                formState.organization
            )
        },
        onLoginClick = { onNavigate(Screen.Login, null) }
    )
}
