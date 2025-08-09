package com.example.school_companion.ui.tab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.data.api.NeedRequestDto
import com.example.school_companion.data.api.NoteRequestDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.SpecialNeed
import com.example.school_companion.data.repository.SessionManager.token
import com.example.school_companion.ui.dialog.need.AddNeedDialog
import com.example.school_companion.ui.dialog.need.EditNeedDialog
import com.example.school_companion.ui.dialog.note.AddNoteDialog
import com.example.school_companion.ui.dialog.note.EditNoteDialog
import com.example.school_companion.ui.viewmodel.NeedsState
import com.example.school_companion.ui.viewmodel.NeedsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SpecialNeedsTab(selectedChild: Child, needsViewModel: NeedsViewModel, token: String) {
    val needsState by needsViewModel.needsState.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Add new Special Need")
        }
        when (needsState) {
            is NeedsState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is NeedsState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Fehler: ${(needsState as NeedsState.Error).message}")
                }
            }

            is NeedsState.Success -> {
                val needs = (needsState as NeedsState.Success).notes

                if (needs.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Keine Special needs für ${selectedChild.name}")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(needs) { need ->
                            ChildSpecialNeedsCard(need, needsViewModel, selectedChild, token)
                        }
                    }
                }
            }
        }
    }
    if (showAddDialog) {
        AddNeedDialog(
            onDismiss = { showAddDialog = false },
            onSave = { need ->
                needsViewModel.createNeed(
                    token,
                    need,
                    selectedChild.id
                )
                showAddDialog = false
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChildSpecialNeedsCard(
    need: SpecialNeed,
    needsViewModel: NeedsViewModel,
    child: Child,
    token: String
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = need.type,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                if (need.description.isNotBlank()) {
                    Text(
                        text = need.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                } else {
                    Text(
                        text = "Keine Beschreibung",
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
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
                    contentDescription = "Edit Special Need",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Special Need",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
    if (showEditDialog) {
        EditNeedDialog(
            need = need,
            onDismiss = { showEditDialog = false },
            onSave = { updatedNeedRequestDto ->
                needsViewModel.updateNeed(
                    token = token,
                    needId = need.id,
                    need = updatedNeedRequestDto,
                    childId = child.id,
                )
                showEditDialog = false
            }
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Special Need?") },
            text = { Text("Are you sure you want to delete «${need.type}»?") },
            confirmButton = {
                Button(
                    onClick = {
                        needsViewModel.deleteNeed(token, need.id, child.id)
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

