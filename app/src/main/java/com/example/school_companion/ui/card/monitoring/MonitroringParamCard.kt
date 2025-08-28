package com.example.school_companion.ui.card.monitoring

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.model.MonitoringParam
import com.example.school_companion.ui.dialog.param.EditParamDialog
import com.example.school_companion.ui.viewmodel.MonitoringParamViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonitoringParamCard(
    param: MonitoringParam,
    paramsViewModel: MonitoringParamViewModel,
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(param.title, fontWeight = FontWeight.Bold)
                Text("Type: ${param.type}")
                Text(param.description)
                Text("Min: ${param.minValue}, Max: ${param.maxValue}")
                Text("Created at: ${param.createdAt}")
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            IconButton(onClick = { showEditDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Monitoring Parameter",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Monitoring Parameter",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        if (showEditDialog) {
            EditParamDialog(
                param = param,
                onDismiss = { showEditDialog = false },
                onSave = { updateParamRequestDto ->
                    paramsViewModel.updateMonitoringParam(
                        param = updateParamRequestDto,
                        paramId = param.id
                    )
                    showEditDialog = false
                }
            )
        }

        if (showDeleteConfirm) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                title = { Text("Delete Monitoring Parameter?") },
                text = { Text("Are you sure you want to delete «${param.title}: ${param.description}»?") },
                confirmButton = {
                    Button(
                        onClick = {
                            paramsViewModel.deleteMonitoringParam(param.id)
                            showDeleteConfirm = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteConfirm = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
