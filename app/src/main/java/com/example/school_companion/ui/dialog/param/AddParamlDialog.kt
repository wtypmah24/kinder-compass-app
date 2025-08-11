package com.example.school_companion.ui.dialog.param

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.api.ParamRequestDto
import com.example.school_companion.data.model.ScaleType

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddParamDialog(
    onDismiss: () -> Unit,
    onSave: (ParamRequestDto) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf<ScaleType?>(null) }
    var minValue by remember { mutableStateOf("") }
    var maxValue by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Monitoring Parameter") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") })

                Box {
                    OutlinedTextField(
                        value = selectedType?.name ?: "Select Type",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true },
                        trailingIcon = {
                            Icon(
                                imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                        enabled = false
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        ScaleType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.name) },
                                onClick = {
                                    selectedType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Min & Max value (only for certain types)
                if (selectedType != ScaleType.QUALITATIVE && selectedType != ScaleType.BINARY) {
                    OutlinedTextField(
                        value = minValue,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) minValue = input
                        },
                        label = { Text("Min Value") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = maxValue,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) maxValue = input
                        },
                        label = { Text("Max Value") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") })
            }
        },

        confirmButton = {
            Button(onClick = {
                selectedType?.let { type ->
                    ParamRequestDto(
                        title = title,
                        type = type,
                        description = description,
                        maxValue = maxValue.toIntOrNull() ?: 0,
                        minValue = minValue.toIntOrNull() ?: 0
                    )
                }?.let { onSave(it) }
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
