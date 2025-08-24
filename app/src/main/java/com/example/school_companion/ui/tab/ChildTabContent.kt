package com.example.school_companion.ui.tab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.school_companion.data.api.DeletePhotoRequestDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.navigation.Screen
import com.example.school_companion.ui.viewmodel.ChildDetailViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChildTabContent(
    index: Int,
    child: Child,
    authToken: String?,
    viewModel: ChildDetailViewModel,
    navController: NavController
) {
    val eventsState by viewModel.events.collectAsStateWithLifecycle()
    val notesState by viewModel.notes.collectAsStateWithLifecycle()
    val needsState by viewModel.needs.collectAsStateWithLifecycle()
    val goalsState by viewModel.goals.collectAsStateWithLifecycle()
    val entriesState by viewModel.entries.collectAsStateWithLifecycle()
    val photosState by viewModel.photos.collectAsStateWithLifecycle()

    when (index) {
        0 -> authToken?.let {
            EventsTab(
                eventsState = eventsState,
                onAddEvent = { dto -> viewModel.createEvent(authToken, child.id, dto) },
                onEditEvent = { event, dto ->
                    viewModel.updateEvent(
                        authToken,
                        child.id,
                        event.id,
                        dto
                    )
                },
                onDeleteEvent = { event -> viewModel.deleteEvent(authToken, event.id, child.id) }
            )
        }

        1 -> authToken?.let {
            MonitoringEntryTab(
                child = child,
                entriesState = entriesState,
                onEditEntry = { id, dto ->
                    viewModel.updateMonitoringEntry(
                        authToken,
                        dto,
                        child.id,
                        id
                    )
                },
                onDeleteEntry = { entry ->
                    viewModel.deleteMonitoringEntry(
                        authToken,
                        entry.id,
                        child.id
                    )
                },
                onAddEntry = { navController.navigate(Screen.Monitoring.route) }
            )
        }

        2 -> authToken?.let {
            NotesTab(
                selectedChild = child,
                notesState = notesState,
                onAddNote = { dto -> viewModel.createNote(authToken, dto, child.id) },
                onEditNote = { note, dto ->
                    viewModel.updateNote(
                        authToken,
                        note.id,
                        dto,
                        child.id
                    )
                },
                onDeleteNote = { note -> viewModel.deleteNote(authToken, note.id, child.id) }
            )
        }

        3 -> authToken?.let {
            SpecialNeedsTab(
                selectedChild = child,
                needsState = needsState,
                onAddNeed = { dto -> viewModel.createNeed(authToken, dto, child.id) },
                onEditNeed = { need, dto ->
                    viewModel.updateNeed(
                        authToken,
                        need.id,
                        dto,
                        child.id
                    )
                },
                onDeleteNeed = { need -> viewModel.deleteNeed(authToken, need.id, child.id) }
            )
        }

        4 -> authToken?.let {
            GoalsTab(
                child = child,
                goalsState = goalsState,
                onAddGoal = { dto -> viewModel.createGoal(authToken, dto, child.id) },
                onEditGoal = { goal, dto ->
                    viewModel.updateGoal(
                        authToken,
                        goal.id,
                        dto,
                        child.id
                    )
                },
                onDeleteGoal = { goal -> viewModel.deleteGoal(authToken, goal.id, child.id) }
            )
        }

        5 -> authToken?.let {
            PhotosTab(
                child = child,
                photosState = photosState,
                onAddPhoto = { filePart, description ->
                    viewModel.addChildPhotos(authToken, child.id, filePart, description)
                },
                onDeletePhoto = { photo ->
                    viewModel.deleteChildPhotos(
                        authToken,
                        DeletePhotoRequestDto(photo.id),
                        child.id
                    )
                }
            )
        }
    }
}
