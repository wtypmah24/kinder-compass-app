package com.example.school_companion.feature.session

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WorkSessionCard(
    workSessionViewModel: WorkSessionViewModel = hiltViewModel()
) {
    val currentSession by workSessionViewModel.session.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        workSessionViewModel.status()
    }

    WorkSessionCardContent(
        currentSession = currentSession,
        onStartSession = { workSessionViewModel.startWorkSession() },
        onEndSession = { workSessionViewModel.endWorkSession() },
        modifier = Modifier.fillMaxWidth()
    )
}