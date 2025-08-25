package com.example.school_companion.ui.dialog.child

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.api.ChildDto
import com.example.school_companion.data.model.Child
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditChildDialog(
    child: Child,
    onDismiss: () -> Unit,
    onSave: (ChildDto) -> Unit
) {
    var name by remember { mutableStateOf(child.name) }
    var surname by remember { mutableStateOf(child.surname) }
    var dateOfBirth by remember {
        mutableStateOf(
            try {
                LocalDate.parse(child.dateOfBirth)
            } catch (e: Exception) {
                null
            }
        )
    }
    var email by remember { mutableStateOf(child.email ?: "") }
    var phoneNumber by remember { mutableStateOf(child.phoneNumber ?: "") }

    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    dateOfBirth?.let {
        calendar.set(it.year, it.monthValue - 1, it.dayOfMonth)
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dateOfBirth = LocalDate.of(year, month + 1, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Child") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("First Name") }
                )
                OutlinedTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Surname") }
                )
                OutlinedTextField(
                    value = dateOfBirth?.toString() ?: "",
                    onValueChange = {},
                    label = { Text("Date of Birth") },
                    readOnly = true,
                    modifier = Modifier.clickable { datePickerDialog.show() }
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (dateOfBirth != null) {
                        onSave(
                            ChildDto(
                                name = name,
                                surname = surname,
                                dateOfBirth = dateOfBirth!!,
                                email = email,
                                phoneNumber = phoneNumber
                            )
                        )
                    }
                }
            ) {
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
