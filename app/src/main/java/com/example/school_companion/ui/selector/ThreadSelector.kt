package com.example.school_companion.ui.selector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.school_companion.ui.dropdown.DropdownMenuWrapper

@Composable
fun ThreadSelector(
    threads: List<String>,
    selectedThread: String?,
    onSelect: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    var threadToDelete by remember { mutableStateOf<String?>(null) }

    DropdownMenuWrapper(items = threads,
        selectedItem = selectedThread,
        onItemSelected = onSelect,
        itemToString = { it },
        placeholder = "Choose Chat Thread",
        customItemContent = { tid ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(tid, modifier = Modifier.weight(1f))
                IconButton(onClick = { threadToDelete = tid }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete chat",
                        tint = Color.Red
                    )
                }
            }
        })

    if (threadToDelete != null) {
        AlertDialog(onDismissRequest = { threadToDelete = null },
            title = { Text("Delete chat with AI?") },
            text = { Text("Are you sure you want to delete ${threadToDelete}?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(threadToDelete!!)
                        threadToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { threadToDelete = null }) {
                    Text("Cancel")
                }
            })
    }
}