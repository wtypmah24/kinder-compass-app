package com.example.school_companion.feature.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.school_companion.ui.util.UiState

@Composable
fun RegisterContent(
    formState: RegisterFormState,
    onFormChange: (RegisterFormState) -> Unit,
    authState: UiState<Unit>,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        RegisterHeader()

        RegisterCard(
            formState = formState,
            onFormChange = onFormChange,
            authState = authState,
            onRegisterClick = onRegisterClick,
            onLoginClick = onLoginClick
        )
    }
}
@Preview(showBackground = true)
@Composable
fun RegisterContentPreview() {
    val sampleFormState = RegisterFormState(
        name = "John",
        surname= "Doe",
        email = "john@example.com",
        password = "password123",
        confirmPassword = "password123",
        organization= "Cool Company"
    )

    val sampleAuthState = UiState.Idle

    RegisterContent(
        formState = sampleFormState,
        onFormChange = {},
        authState = sampleAuthState,
        onRegisterClick = {},
        onLoginClick = {}
    )
}
