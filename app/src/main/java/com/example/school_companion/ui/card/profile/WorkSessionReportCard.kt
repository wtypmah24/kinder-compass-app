package com.example.school_companion.ui.card.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.school_companion.ui.button.DatePickerButton
import com.example.school_companion.ui.viewmodel.SessionsState
import com.example.school_companion.ui.viewmodel.WorkSessionViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkSessionReportCard(
    authToken: String?, workSessionViewModel: WorkSessionViewModel
) {
    var startDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    var endDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    val sessionsState by workSessionViewModel.sessions.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
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
                    initialDate = startDate ?: LocalDate.now()
                ) { startDate = it }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("End Date: ")
                DatePickerButton(
                    initialDate = endDate ?: LocalDate.now()
                ) { endDate = it }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (authToken != null && startDate != null && endDate != null) {
                        workSessionViewModel.report(authToken, startDate!!, endDate!!)
                    }
                }, enabled = startDate != null && endDate != null
            ) {
                Text("Get Report")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (sessionsState) {
                is SessionsState.Loading -> Text("Choose the dates and press \"Get Report\"")
                is SessionsState.Error -> Text(
                    "Error: ${(sessionsState as SessionsState.Error).message}",
                    color = MaterialTheme.colorScheme.error
                )

                is SessionsState.Success -> {
                    val sessions = (sessionsState as SessionsState.Success).sessions
                    Column {
                        sessions.forEach { session ->
                            if (authToken != null) {
                                startDate?.let {
                                    endDate?.let { it1 ->
                                        WorkSessionItemCard(
                                            session,
                                            workSessionViewModel,
                                            authToken,
                                            it,
                                            it1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}