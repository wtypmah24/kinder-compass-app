package com.example.school_companion.feature.session

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.api.SessionApi.SessionUpdateDto
import com.example.school_companion.data.model.WorkSession

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkSessionItemCard(
    session: WorkSession,
    onSessionUpdate: (SessionUpdateDto) -> Unit,
    onSessionDelete: () -> Unit,
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Start: ${session.startTime}")
                Text("End: ${session.endTime}")
                Text("Note: ${session.note}")
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
                    contentDescription = "Edit Work Session",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Work Session",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
    if (showEditDialog) {
        EditSessionDialog(
            session = session,
            onDismiss = { showEditDialog = false },
            onSave = { dto ->
                onSessionUpdate
                showEditDialog = false
            }
        )
    }
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete work session?") },
            text = { Text("Are you sure you want to delete «${session.startTime} -- ${session.endTime}»?") },
            confirmButton = {
                Button(
                    onClick = {
                        onSessionDelete
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
            })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun WorkSessionItemCardPreview() {
    val sampleSession = WorkSession(
        id = 1L,
        startTime = "2025-09-16 09:00",
        endTime = "2025-09-16 17:00",
        note = "Team meeting and project work"
    )

    MaterialTheme {
        WorkSessionItemCard(
            session = sampleSession,
            onSessionUpdate = { dto ->
                // Preview stub
            },
            onSessionDelete = {
                // Preview stub
            }
        )
    }
}
