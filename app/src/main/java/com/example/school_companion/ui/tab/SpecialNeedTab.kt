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
import com.example.school_companion.ui.card.child.ChildSpecialNeedsCard
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
