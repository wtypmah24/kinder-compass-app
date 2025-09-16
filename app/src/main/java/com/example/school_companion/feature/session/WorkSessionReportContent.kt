package com.example.school_companion.feature.session

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.school_companion.data.api.SessionApi.SessionUpdateDto
import com.example.school_companion.data.model.WorkSession
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkSessionReportContent(
    startDate: LocalDate?,
    endDate: LocalDate?,
    sessionsState: SessionsState,
    onStartDateChange: (LocalDate) -> Unit,
    onEndDateChange: (LocalDate) -> Unit,
    onGetReport: () -> Unit,
    onUpdateSession: (Long, SessionUpdateDto) -> Unit,
    onDeleteSession: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Work session reports",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Start Date: ")
                DatePickerButton(
                    initialDate = startDate ?: LocalDate.now(),
                    onDateSelected = onStartDateChange
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("End Date: ")
                DatePickerButton(
                    initialDate = endDate ?: LocalDate.now(),
                    onDateSelected = onEndDateChange
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onGetReport,
                enabled = startDate != null && endDate != null,
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Get Report",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Get Report",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (sessionsState) {
                is SessionsState.Loading -> Text("Choose the dates and press \"Get Report\"")
                is SessionsState.Error -> Text(
                    "Error: ${sessionsState.message}",
                    color = MaterialTheme.colorScheme.error
                )

                is SessionsState.Success -> {
                    val sessions = sessionsState.sessions
                    Column {
                        sessions.forEach { session ->
                            WorkSessionItemCard(
                                session = session,
                                onSessionUpdate = { dto -> onUpdateSession(session.id, dto) },
                                onSessionDelete = { onDeleteSession(session.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun WorkSessionReportContentPreview() {
    val sampleSessions = listOf(
        WorkSession(
            id = 1L,
            startTime = "2025-09-16 09:00",
            endTime = "2025-09-16 17:00",
            note = "Project meeting"
        ),
        WorkSession(
            id = 2L,
            startTime = "2025-09-17 09:30",
            endTime = "2025-09-17 16:30",
            note = "Coding session"
        ),
        WorkSession(
            id = 3L,
            startTime = "2025-09-18 10:00",
            endTime = "2025-09-18 15:30",
            note = "Client call"
        )
    )

    WorkSessionReportContent(
        startDate = LocalDate.of(2025, 9, 16),
        endDate = LocalDate.of(2025, 9, 18),
        sessionsState = SessionsState.Success(sessions = sampleSessions),
        onStartDateChange = {},
        onEndDateChange = {},
        onGetReport = {},
        onUpdateSession = { _, _ -> },
        onDeleteSession = {}
    )
}
