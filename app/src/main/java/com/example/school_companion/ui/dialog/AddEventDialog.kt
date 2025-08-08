package com.example.school_companion.ui.dialog

import android.os.Build
import android.util.Log
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
import androidx.compose.ui.unit.dp
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEventDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, Instant, Instant, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var startDateTime by remember { mutableStateOf("") }
    var endDateTime by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Event") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })
                OutlinedTextField(value = startDateTime, onValueChange = { startDateTime = it }, label = { Text("Start DateTime (ISO)") })
                OutlinedTextField(value = endDateTime, onValueChange = { endDateTime = it }, label = { Text("End DateTime (ISO)") })
            }
        },
        confirmButton = {
            Button(onClick = {
                try {
                    val start = Instant.parse(startDateTime)
                    val end = Instant.parse(endDateTime)
                    onSave(title, description, start, end, location)
                } catch (e: Exception) {
                    Log.e("AddEventDialog", "Invalid date format", e)
                }
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
