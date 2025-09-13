package com.example.school_companion.feature.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.feature.auth.AuthViewModel
import com.example.school_companion.feature.session.WorkSessionCard
import com.example.school_companion.feature.session.WorkSessionReportCard
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.navigation.Screen
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.util.onState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    onNavigate: NavigateToWithArgs,
    authViewModel: AuthViewModel,
    companionViewModel: CompanionViewModel = hiltViewModel()
) {
    val currentUserState by authViewModel.currentCompanion.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        ProfileTopBar(
            onBack = { onNavigate(null, null) },
            onLogout = { onNavigate(Screen.Login, null) }
        )
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                currentUserState.onState(
                    onLoading = { LoadingBox() },
                    onError = { msg -> ErrorBox(message = msg) },
                    onSuccess = { user ->
                        UserInfoCard(
                            currentUser = user,
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
                    },
                )
            }
            item {
                WorkSessionCard()
            }
            item {
                WorkSessionReportCard()
            }
        }
    }
}
