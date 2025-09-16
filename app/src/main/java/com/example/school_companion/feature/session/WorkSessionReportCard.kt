package com.example.school_companion.feature.session

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkSessionReportCard(
    workSessionViewModel: WorkSessionViewModel = hiltViewModel()
) {
    var startDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    var endDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    val sessionsState by workSessionViewModel.sessions.collectAsState()

    WorkSessionReportContent(
        startDate = startDate,
        endDate = endDate,
        sessionsState = sessionsState,
        onStartDateChange = { startDate = it },
        onEndDateChange = { endDate = it },
        onGetReport = {
            if (startDate != null && endDate != null) {
                workSessionViewModel.report(startDate!!, endDate!!)
            }
        },
        onUpdateSession = { sessionId, dto ->
            if (startDate != null && endDate != null) {
                workSessionViewModel.update(sessionId, dto, startDate!!, endDate!!)
            }
        },
        onDeleteSession = { sessionId ->
            if (startDate != null && endDate != null) {
                workSessionViewModel.delete(sessionId, startDate!!, endDate!!)
            }
        }
    )
}
