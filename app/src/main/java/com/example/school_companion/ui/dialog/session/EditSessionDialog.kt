package com.example.school_companion.ui.dialog.session

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.api.ParamRequestDto
import com.example.school_companion.data.api.SessionApi.SessionUpdateDto
import com.example.school_companion.data.model.ScaleType
import com.example.school_companion.data.model.WorkSession
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditSessionDialog(
    session: WorkSession,
    onDismiss: () -> Unit,
    onSave: (SessionUpdateDto) -> Unit
) {
    val startDateTime = remember(session.startTime) {
        session.startTime
            ?.takeIf { it.isNotBlank() }
            ?.let { Instant.parse(it).atZone(ZoneId.systemDefault()).toLocalDateTime() }
            ?: LocalDateTime.now()
    }

    val endDateTime = remember(session.endTime) {
        session.endTime
            ?.takeIf { it.isNotBlank() }
            ?.let { Instant.parse(it).atZone(ZoneId.systemDefault()).toLocalDateTime() }
            ?: LocalDateTime.now().plusHours(1)
    }

    var startDate by remember { mutableStateOf(startDateTime.toLocalDate()) }
    var startTime by remember { mutableStateOf(startDateTime.toLocalTime()) }
    var endDate by remember { mutableStateOf(endDateTime.toLocalDate()) }
    var endTime by remember { mutableStateOf(endDateTime.toLocalTime()) }

    val context = LocalContext.current
    var notes by remember { mutableStateOf(session.note ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Work Session") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val newStart = LocalDateTime.of(startDate, startTime)
                val newEnd = LocalDateTime.of(endDate, endTime)

                onSave(
                    SessionUpdateDto(
                        startTime = newStart.toString(),
                        endTime = newEnd.toString(),
                        note = notes
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
