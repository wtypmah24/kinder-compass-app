package com.example.school_companion.feature.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.feature.auth.AuthViewModel
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.navigation.Screen
import com.example.school_companion.ui.message.ErrorMessage
import com.example.school_companion.ui.util.UiState

@Composable
fun LoginScreen(
    onNavigate: NavigateToWithArgs,
    viewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        if (authState is UiState.Success) {
            onNavigate(Screen.Dashboard, null)
        }
    }

    val loginEnabled =
        email.isNotBlank() && password.isNotBlank() && authState !is UiState.Loading
    val loginLoading = authState is UiState.Loading
    val errorMessage = (authState as? UiState.Error)?.message

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginHeader()

            LoginCard {
                Text(
                    text = "Anmelden",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                LoginFields(
                    email = email,
                    onEmailChange = { email = it },
                    password = password,
                    onPasswordChange = { password = it },
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChange = { passwordVisible = it }
                )

                if (errorMessage != null) {
                    ErrorMessage(errorMessage)
                }

                LoginActions(
                    loginEnabled = loginEnabled,
                    loginLoading = loginLoading,
                    onLoginClick = { viewModel.login(email, password) },
                    onRegisterClick = { onNavigate(Screen.Register, null) }
                )
            }
        }
    }
}
