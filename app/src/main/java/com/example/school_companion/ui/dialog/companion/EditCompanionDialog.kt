package com.example.school_companion.ui.dialog.companion

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
import com.example.school_companion.data.api.CompanionUpdateDto
import com.example.school_companion.data.model.Companion
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditCompanionDialog(
    companion: Companion,
    onDismiss: () -> Unit,
    onSave: (CompanionUpdateDto) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(companion.name) }
    var surname by remember { mutableStateOf(companion.surname) }
    var email by remember { mutableStateOf(companion.email) }
    var organization by remember { mutableStateOf(companion.organization ?: "") }
    var startWorkingTime by remember {
        mutableStateOf(companion.startWorkingTime?.let { LocalTime.parse(it) })
    }
    var endWorkingTime by remember {
        mutableStateOf(companion.endWorkingTime?.let { LocalTime.parse(it) })
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Companion data") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") })
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Last name") })
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") })
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = organization,
                    onValueChange = { organization = it },
                    label = { Text("Organization") })
            }
            Button(onClick = {
                startWorkingTime?.let {
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            startWorkingTime = LocalTime.of(hour, minute)
                        },
                        it.hour, startWorkingTime!!.minute, true
                    )
                }?.show()
            }) {
                Text("Start Time: ${startWorkingTime ?: "Not set"}")
            }

            Button(onClick = {
                endWorkingTime?.let {
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            endWorkingTime = LocalTime.of(hour, minute)
                        },
                        it.hour, endWorkingTime!!.minute, true
                    )
                }?.show()
            }) {
                Text("End Time: ${endWorkingTime ?: "Not set"}")
            }

        },

        confirmButton = {
            Button(onClick = {
                onSave(
                    CompanionUpdateDto(
                        email = email,
                        name = name,
                        surname = surname,
                        organization = organization,
                        startWorkingTime = startWorkingTime,
                        endWorkingTime = endWorkingTime
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
