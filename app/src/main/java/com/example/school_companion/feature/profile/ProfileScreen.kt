package com.example.school_companion.feature.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.feature.auth.AuthViewModel
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.navigation.Screen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    onNavigate: NavigateToWithArgs,
    authViewModel: AuthViewModel,
    companionViewModel: CompanionViewModel = hiltViewModel()
) {
    val currentUserState by authViewModel.currentCompanion.collectAsStateWithLifecycle()

    ProfileContent(
        currentUserState = currentUserState,
        onBack = { onNavigate(null, null) },
        onLogout = { onNavigate(Screen.Login, null) },
        onUpdateInfo = { dto ->
            companionViewModel.updateCompanion(dto) {
                authViewModel.getUserProfile()
            }
        },
        onUpdatePassword = { dto ->
            companionViewModel.updatePassword(dto)
        },
        onDeleteAccount = {
            companionViewModel.deleteCompanion {
                authViewModel.logout()
            }
        }
    )
}