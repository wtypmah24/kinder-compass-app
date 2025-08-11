package com.example.school_companion.ui.dialog.entry

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.api.EntryRequestDto
import com.example.school_companion.data.model.MonitoringParam
import com.example.school_companion.data.model.ScaleType

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEntryDialog(
    param: MonitoringParam,
    onDismiss: () -> Unit,
    onSave: (EntryRequestDto) -> Unit
) {
    var value by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Monitoring Entry") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                when (param.type) {
                    ScaleType.QUALITATIVE -> {
                        OutlinedTextField(
                            value = value,
                            onValueChange = { value = it },
                            label = { Text("Value") }
                        )
                    }

                    ScaleType.BINARY -> {
                        val options = listOf("YES", "NO")
                        var expanded by remember { mutableStateOf(false) }

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = value,
                                onValueChange = { },
                                label = { Text("Value") },
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                options.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            value = option
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    else -> {
                        OutlinedTextField(
                            value = value,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() || it == '.' }) {
                                    value = newValue
                                }
                            },
                            label = { Text("Value") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
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
                onSave(EntryRequestDto(value, notes))
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
