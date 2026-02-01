package com.example.school_companion.feature.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.api.CompanionUpdateDto
import com.example.school_companion.data.api.PasswordUpdateDto
import com.example.school_companion.data.model.Companion
import com.example.school_companion.feature.session.WorkSessionCard
import com.example.school_companion.feature.session.WorkSessionReportCard
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.util.UiState
import com.example.school_companion.ui.util.onState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileContent(
    currentUserState: UiState<Companion>,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onUpdateInfo: (CompanionUpdateDto) -> Unit,
    onUpdatePassword: (PasswordUpdateDto) -> Unit,
    onDeleteAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            ProfileTopBar(
                onBack = onBack,
                onLogout = onLogout
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
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
                            onUpdateInfo = onUpdateInfo,
                            onUpdatePassword = onUpdatePassword,
                            onDeleteAccount = onDeleteAccount
                        )
                    },
                )
            }
            item { WorkSessionCard() }
            item { WorkSessionReportCard() }
        }
    }
}
