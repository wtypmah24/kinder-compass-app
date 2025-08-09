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
import com.example.school_companion.data.model.Event
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditEventDialog(
    event: Event,
    onDismiss: () -> Unit,
    onSave: (EventRequestDto) -> Unit
) {
    var title by remember { mutableStateOf(event.title) }
    var description by remember { mutableStateOf(event.description) }
    var location by remember { mutableStateOf(event.location) }

    val startDateTime = remember(event.startDateTime) {
        event.startDateTime
            ?.takeIf { it.isNotBlank() }
            ?.let { Instant.parse(it).atZone(ZoneId.systemDefault()).toLocalDateTime() }
            ?: LocalDateTime.now()
    }

    val endDateTime = remember(event.endDateTime) {
        event.endDateTime
            ?.takeIf { it.isNotBlank() }
            ?.let { Instant.parse(it).atZone(ZoneId.systemDefault()).toLocalDateTime() }
            ?: LocalDateTime.now().plusHours(1)
    }


    var startDate by remember { mutableStateOf(startDateTime.toLocalDate()) }
    var startTime by remember { mutableStateOf(startDateTime.toLocalTime()) }
    var endDate by remember { mutableStateOf(endDateTime.toLocalDate()) }
    var endTime by remember { mutableStateOf(endDateTime.toLocalTime()) }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Event") },
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

                Button(onClick = {
                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            startDate = LocalDate.of(year, month + 1, day)
                        },
                        startDate.year, startDate.monthValue - 1, startDate.dayOfMonth
                    ).show()
                }) {
                    Text("Start Date: $startDate")
                }

                Button(onClick = {
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            startTime = LocalTime.of(hour, minute)
                        },
                        startTime.hour, startTime.minute, true
                    ).show()
                }) {
                    Text("Start Time: $startTime")
                }

                Button(onClick = {
                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            endDate = LocalDate.of(year, month + 1, day)
                        },
                        endDate.year, endDate.monthValue - 1, endDate.dayOfMonth
                    ).show()
                }) {
                    Text("End Date: $endDate")
                }

                Button(onClick = {
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            endTime = LocalTime.of(hour, minute)
                        },
                        endTime.hour, endTime.minute, true
                    ).show()
                }) {
                    Text("End Time: $endTime")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val startInstant =
                    LocalDateTime.of(startDate, startTime).atZone(ZoneId.systemDefault())
                        .toInstant()
                val endInstant =
                    LocalDateTime.of(endDate, endTime).atZone(ZoneId.systemDefault()).toInstant()

                onSave(
                    EventRequestDto(
                        title = title,
                        description = description,
                        startDateTime = startInstant.toString(),
                        endDateTime = endInstant.toString(),
                        location = location
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
