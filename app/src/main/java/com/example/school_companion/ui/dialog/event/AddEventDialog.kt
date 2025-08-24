package com.example.school_companion.ui.dialog.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.api.EventRequestDto
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEventDialog(
    onDismiss: () -> Unit,
    onSave: (EventRequestDto) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var startTime by remember { mutableStateOf(LocalTime.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }
    var endTime by remember { mutableStateOf(LocalTime.now().plusHours(1)) }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Event") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") })
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") })
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") })

                // Start Date
                Button(onClick = {
                    val dp = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            startDate = LocalDate.of(year, month + 1, dayOfMonth)
                        },
                        startDate.year, startDate.monthValue - 1, startDate.dayOfMonth
                    )
                    dp.show()
                }) {
                    Text("Start Date: $startDate")
                }

                // Start Time
                Button(onClick = {
                    val tp = TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            startTime = LocalTime.of(hour, minute)
                        },
                        startTime.hour, startTime.minute, true
                    )
                    tp.show()
                }) {
                    Text("Start Time: $startTime")
                }

                // End Date
                Button(onClick = {
                    val dp = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            endDate = LocalDate.of(year, month + 1, dayOfMonth)
                        },
                        endDate.year, endDate.monthValue - 1, endDate.dayOfMonth
                    )
                    dp.show()
                }) {
                    Text("End Date: $endDate")
                }

                // End Time
                Button(onClick = {
                    val tp = TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            endTime = LocalTime.of(hour, minute)
                        },
                        endTime.hour, endTime.minute, true
                    )
                    tp.show()
                }) {
                    Text("End Time: $endTime")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val startInstant = LocalDateTime.of(startDate, startTime)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                val endInstant = LocalDateTime.of(endDate, endTime)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()

                onSave(
                    EventRequestDto(
                        title,
                        description,
                        startDateTime = startInstant.toString(),
                        endDateTime = endInstant.toString(),
                        location
                    )
                )
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
