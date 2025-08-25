package com.example.school_companion.ui.tab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.data.api.NoteRequestDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.card.child.ChildNoteCard
import com.example.school_companion.ui.dialog.note.AddNoteDialog
import com.example.school_companion.ui.viewmodel.NotesState
import com.example.school_companion.ui.viewmodel.NotesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesTab(
    child: Child,
    token: String,
    viewModel: NotesViewModel = hiltViewModel(),
) {
    val notesState by viewModel.notesState.collectAsStateWithLifecycle()
    LaunchedEffect(token, child) {
        viewModel.loadNotes(token, child.id)
    }

    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Add new note")
        }

        when (notesState) {
            is NotesState.Loading -> LoadingBox()
            is NotesState.Error -> ErrorBox((notesState as NotesState.Error).message)

            is NotesState.Success -> {
                val notes = (notesState as NotesState.Success).notes
                if (notes.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Keine Notes für ${child.name}")
                    }
                } else {
                    LazyColumn {
                        items(notes) { note ->
                            ChildNoteCard(
                                note = note,
                                onEdit = { dto ->
                                    viewModel.updateNote(
                                        token,
                                        note.id,
                                        dto,
                                        child.id
                                    )
                                },
                                onDelete = { viewModel.deleteNote(token, note.id, child.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddNoteDialog(
            onDismiss = { showAddDialog = false },
            onSave = { content ->
                viewModel.createNote(token, NoteRequestDto(content), child.id)
                showAddDialog = false
            }
        )
    }
}
