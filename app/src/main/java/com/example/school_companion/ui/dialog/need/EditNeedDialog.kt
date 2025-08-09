package com.example.school_companion.ui.dialog.need

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
import com.example.school_companion.data.api.NeedRequestDto
import com.example.school_companion.data.model.Note
import com.example.school_companion.data.model.SpecialNeed

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditNeedDialog(
    need: SpecialNeed,
    onDismiss: () -> Unit,
    onSave: (NeedRequestDto) -> Unit
) {
    var type by remember { mutableStateOf(need.type) }
    var desciption by remember { mutableStateOf(need.description) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Special Need") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Type") })
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = desciption,
                    onValueChange = { desciption = it },
                    label = { Text("Description") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    NeedRequestDto(type, desciption)
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
