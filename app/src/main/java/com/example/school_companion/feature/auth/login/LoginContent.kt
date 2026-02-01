package com.example.school_companion.feature.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.school_companion.ui.message.ErrorMessage

@Composable
fun LoginContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    loginEnabled: Boolean,
    loginLoading: Boolean,
    errorMessage: String?,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
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
                onEmailChange = onEmailChange,
                password = password,
                onPasswordChange = onPasswordChange,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = onPasswordVisibilityChange
            )

            if (errorMessage != null) {
                ErrorMessage(errorMessage)
            }

            LoginActions(
                loginEnabled = loginEnabled,
                loginLoading = loginLoading,
                onLoginClick = onLoginClick,
                onRegisterClick = onRegisterClick
            )
        }
    }
}

@Preview(showBackground = true, name = "Normal State")
@Composable
fun LoginContentPreview_Normal() {
    LoginContent(
        email = "john@example.com",
        onEmailChange = {},
        password = "password123",
        onPasswordChange = {},
        passwordVisible = false,
        onPasswordVisibilityChange = {},
        loginEnabled = true,
        loginLoading = false,
        errorMessage = null,
        onLoginClick = {},
        onRegisterClick = {}
    )
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun LoginContentPreview_Error() {
    LoginContent(
        email = "john@example.com",
        onEmailChange = {},
        password = "password123",
        onPasswordChange = {},
        passwordVisible = true,
        onPasswordVisibilityChange = {},
        loginEnabled = true,
        loginLoading = false,
        errorMessage = "Wrong pass",
        onLoginClick = {},
        onRegisterClick = {}
    )
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun LoginContentPreview_Loading() {
    LoginContent(
        email = "john@example.com",
        onEmailChange = {},
        password = "password123",
        onPasswordChange = {},
        passwordVisible = false,
        onPasswordVisibilityChange = {},
        loginEnabled = false,
        loginLoading = true,
        errorMessage = null,
        onLoginClick = {},
        onRegisterClick = {}
    )
}
