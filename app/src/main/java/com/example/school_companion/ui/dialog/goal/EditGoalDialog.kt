package com.example.school_companion.ui.dialog.goal

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
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.api.GoalRequestDto
import com.example.school_companion.data.api.NoteRequestDto
import com.example.school_companion.data.model.Goal
import com.example.school_companion.data.model.Note

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditGoalDialog(
    goal: Goal,
    onDismiss: () -> Unit,
    onSave: (GoalRequestDto) -> Unit
) {
    var description by remember { mutableStateOf(goal.description) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Event") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    GoalRequestDto(description)
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
