package com.example.school_companion.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.api.ChildRequestDto
import com.example.school_companion.data.api.DeletePhotoRequestDto
import com.example.school_companion.data.api.EntryRequestDto
import com.example.school_companion.data.api.EventRequestDto
import com.example.school_companion.data.api.GoalRequestDto
import com.example.school_companion.data.api.NeedRequestDto
import com.example.school_companion.data.api.NoteRequestDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.Event
import com.example.school_companion.data.model.EventWithChild
import com.example.school_companion.data.model.Goal
import com.example.school_companion.data.model.MonitoringEntry
import com.example.school_companion.data.model.Note
import com.example.school_companion.data.model.Photo
import com.example.school_companion.data.model.SpecialNeed
import com.example.school_companion.data.repository.ChildrenRepository
import com.example.school_companion.data.repository.EventsRepository
import com.example.school_companion.data.repository.GoalsRepository
import com.example.school_companion.data.repository.MonitoringEntryRepository
import com.example.school_companion.data.repository.NotesRepository
import com.example.school_companion.data.repository.SpecialNeedsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChildDetailViewModel @Inject constructor(
    private val childrenRepository: ChildrenRepository,
    private val eventsRepository: EventsRepository,
    private val notesRepository: NotesRepository,
    private val needsRepository: SpecialNeedsRepository,
    private val goalsRepository: GoalsRepository,
    private val entriesRepository: MonitoringEntryRepository,
) : ViewModel() {

    private val _child = MutableStateFlow<UiState<Child>>(UiState.Loading)
    val child: StateFlow<UiState<Child>> = _child

    private val _children = MutableStateFlow<UiState<List<Child>>>(UiState.Loading)
    val children: StateFlow<UiState<List<Child>>> = _children

    private val _events = MutableStateFlow<UiState<List<Event>>>(UiState.Loading)
    val events: StateFlow<UiState<List<Event>>> = _events

    private val _eventsWithChildren =
        MutableStateFlow<UiState<List<EventWithChild>>>(UiState.Loading)
    val eventsWithChildren: StateFlow<UiState<List<EventWithChild>>> =
        _eventsWithChildren.asStateFlow()

    private val _notes = MutableStateFlow<UiState<List<Note>>>(UiState.Loading)
    val notes: StateFlow<UiState<List<Note>>> = _notes

    private val _needs = MutableStateFlow<UiState<List<SpecialNeed>>>(UiState.Loading)
    val needs: StateFlow<UiState<List<SpecialNeed>>> = _needs

    private val _goals = MutableStateFlow<UiState<List<Goal>>>(UiState.Loading)
    val goals: StateFlow<UiState<List<Goal>>> = _goals

    private val _entries = MutableStateFlow<UiState<List<MonitoringEntry>>>(UiState.Loading)
    val entries: StateFlow<UiState<List<MonitoringEntry>>> = _entries

    private val _photos = MutableStateFlow<UiState<List<Photo>>>(UiState.Loading)
    val photos: StateFlow<UiState<List<Photo>>> = _photos


    fun loadAll(token: String, childId: Long) {
        viewModelScope.launch {
            coroutineScope {
                launch {
                    childrenRepository.getChild(token, childId).collect { result ->
                        _child.value = result.fold(
                            onSuccess = { UiState.Success(it) },
                            onFailure = { UiState.Error(it.message!!) }
                        )
                    }
                }
                launch {
                    childrenRepository.getChildPhotos(token, childId).collect { result ->
                        _photos.value = result.fold(
                            onSuccess = { UiState.Success(it) },
                            onFailure = { UiState.Error(it.message!!) }
                        )
                    }
                }
                launch {
                    eventsRepository.getEventsByChild(token, childId).collect { result ->
                        _events.value = result.fold(
                            onSuccess = { UiState.Success(it) },
                            onFailure = { UiState.Error(it.message!!) }
                        )
                    }
                }
                launch {
                    notesRepository.getNotes(token, childId).collect { result ->
                        _notes.value = result.fold(
                            onSuccess = { UiState.Success(it) },
                            onFailure = { UiState.Error(it.message!!) }
                        )
                    }
                }
                launch {
                    needsRepository.getNeeds(token, childId).collect { result ->
                        _needs.value = result.fold(
                            onSuccess = { UiState.Success(it) },
                            onFailure = { UiState.Error(it.message!!) }
                        )
                    }
                }
                launch {
                    goalsRepository.getGoals(token, childId).collect { result ->
                        _goals.value = result.fold(
                            onSuccess = { UiState.Success(it) },
                            onFailure = { UiState.Error(it.message!!) }
                        )
                    }
                }
                launch {
                    entriesRepository.getMonitoringEntryData(token, childId).collect { result ->
                        _entries.value = result.fold(
                            onSuccess = { UiState.Success(it) },
                            onFailure = { UiState.Error(it.message!!) }
                        )
                    }
                }
            }
        }
    }

    // Children
    fun addChild(child: ChildRequestDto, token: String) {
        viewModelScope.launch {
            childrenRepository.addChild(token, child).collect { result ->
                result.fold(
                    onSuccess = {
                        // Refresh events list
                        loadChildren(token)
                    },
                    onFailure = { exception ->
                        _child.value =
                            UiState.Error(exception.message ?: "Failed to create child")
                        loadChildren(token)
                    }
                )
            }
        }
    }

    fun updateChild(token: String, childId: Long, child: ChildRequestDto) {
        viewModelScope.launch {
            childrenRepository.updateChild(token, childId, child).collect { result ->
                result.fold(
                    onSuccess = {
                        // Refresh events list
                        loadChildren(token)
                    },
                    onFailure = { exception ->
                        _child.value =
                            UiState.Error(exception.message ?: "Failed to update child")
                        loadChildren(token)
                    }
                )
            }
        }
    }

    fun deleteChild(token: String, childId: Long) {
        viewModelScope.launch {
            childrenRepository.deleteChild(token, childId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadChildren(token)
                    },
                    onFailure = { exception ->
                        _child.value =
                            UiState.Error(exception.message ?: "Failed to delete child")
                        loadChildren(token)
                    }
                )
            }
        }
    }

    fun loadChildren(token: String) {
        viewModelScope.launch {
            _child.value = UiState.Loading
            childrenRepository.getChildren(token).collect { result ->
                result.fold(
                    onSuccess = { children ->
                        _children.value = UiState.Success(children)
                    },
                    onFailure = { exception ->
                        _children.value =
                            UiState.Error(exception.message ?: "Failed to load children")
                    }
                )
            }
        }
    }

    // Photos
    fun loadChildPhotos(token: String, childId: Long) {
        viewModelScope.launch {
            childrenRepository.getChildPhotos(token, childId).collect { result ->
                result.fold(
                    onSuccess = { photos ->
                        _photos.value = UiState.Success(photos)
                    },
                    onFailure = { exception ->
                        // Handle error
                    }
                )
            }
        }
    }

    fun addChildPhotos(
        token: String,
        childId: Long,
        file: File,
        descriptionText: String
    ) {
        viewModelScope.launch {
            childrenRepository.addChildPhotos(token, childId, file, descriptionText)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            loadChildPhotos(token, childId)
                        },
                        onFailure = { exception ->
                            _child.value =
                                UiState.Error(exception.message ?: "Failed to add photo")
                            loadChildPhotos(token, childId)
                        }
                    )
                }
        }
    }

    fun deleteChildPhotos(
        token: String,
        deletePhotoRequestDto: DeletePhotoRequestDto,
        childId: Long
    ) {
        viewModelScope.launch {
            childrenRepository.deleteChildPhoto(token, deletePhotoRequestDto)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            loadChildPhotos(token, childId)
                        },
                        onFailure = { exception ->
                            _child.value =
                                UiState.Error(exception.message ?: "Failed to delete photo")
                            loadChildPhotos(token, childId)
                        }
                    )
                }
        }
    }

    // Events
    fun loadEventsByCompanion(token: String) {
        viewModelScope.launch {
            _events.value = UiState.Loading

            eventsRepository.getEventsByCompanion(token).collect { result ->
                result.fold(
                    onSuccess = { events ->
                        _events.value = UiState.Success(events)
                    },
                    onFailure = { exception ->
                        _events.value =
                            UiState.Error(exception.message ?: "Failed to load events")
                    }
                )
            }
        }
    }

    fun loadEventsWithChildren(token: String) {
        viewModelScope.launch {
            _eventsWithChildren.value = UiState.Loading

            try {
                val eventsResult = eventsRepository.getEventsByCompanion(token).first()

                if (eventsResult.isFailure) {
                    val errorMsg =
                        eventsResult.exceptionOrNull()?.message ?: "Failed to load events"
                    _eventsWithChildren.value = UiState.Error(errorMsg)
                    return@launch
                }

                val events = eventsResult.getOrThrow()

                if (events.isEmpty()) {
                    _eventsWithChildren.value = UiState.Success(emptyList())
                    return@launch
                }

                val childIds = events.map { it.childId }.distinct()

                val children = childIds.map { id ->
                    async {
                        try {
                            val childResult = childrenRepository.getChild(token, id).first()
                            childResult.getOrNull()
                        } catch (e: Exception) {
                            null
                        }
                    }
                }.awaitAll().filterNotNull()

                val mapped = events.mapNotNull { event ->
                    val child = children.find { it.id == event.childId }
                    child?.let { EventWithChild(event, it) }
                }

                _eventsWithChildren.value = UiState.Success(mapped)

            } catch (e: Exception) {
                Log.e("EventsVM", "Exception in loadEventsWithChildren: ${e.message}", e)
                _eventsWithChildren.value =
                    UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadEventsByChild(token: String, childId: Long) {
        viewModelScope.launch {
            _events.value = UiState.Loading

            eventsRepository.getEventsByChild(token, childId).collect { result ->
                result.fold(
                    onSuccess = { events ->
                        _events.value = UiState.Success(events)
                    },
                    onFailure = { exception ->
                        _events.value =
                            UiState.Error(exception.message ?: "Failed to load event")
                        loadEventsByChild(token, childId)
                    }
                )
            }
        }
    }

    fun updateEvent(token: String, childId: Long, eventId: Long, event: EventRequestDto) {
        viewModelScope.launch {
            eventsRepository.updateEvent(token, childId, eventId, event).collect { result ->
                result.fold(
                    onSuccess = {
                        loadEventsByChild(token, childId)
                    },
                    onFailure = { exception ->
                        _events.value =
                            UiState.Error(exception.message ?: "Failed to update event")
                        loadEventsByChild(token, childId)
                    }
                )
            }
        }
    }

    fun deleteEvent(token: String, eventId: Long, childId: Long) {
        viewModelScope.launch {
            eventsRepository.deleteEvent(token, eventId, childId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadEventsByChild(token, childId)
                    },
                    onFailure = { exception ->
                        _events.value =
                            UiState.Error(exception.message ?: "Failed to delete event")
                        loadEventsByChild(token, childId)
                    }
                )
            }
        }
    }

    fun createEvent(token: String, childId: Long, event: EventRequestDto) {
        viewModelScope.launch {
            eventsRepository.createEvent(token, childId, event).collect { result ->
                result.fold(
                    onSuccess = {
                        loadEventsByChild(token, childId)
                    },
                    onFailure = { exception ->
                        _events.value =
                            UiState.Error(exception.message ?: "Failed to create event")
                        loadEventsByChild(token, childId)
                    }
                )
            }
        }
    }

    // Goals
    fun loadGoals(token: String, childId: Long) {
        viewModelScope.launch {
            _goals.value = UiState.Loading

            goalsRepository.getGoals(token, childId).collect { result ->
                result.fold(
                    onSuccess = { notes ->
                        _goals.value = UiState.Success(notes)
                    },
                    onFailure = { exception ->
                        _goals.value =
                            UiState.Error(exception.message ?: "Failed to load notes")
                    }
                )
            }
        }
    }

    fun createGoal(token: String, goal: GoalRequestDto, childId: Long) {
        viewModelScope.launch {
            goalsRepository.createGoal(token, goal, childId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadGoals(token, childId)
                    },
                    onFailure = { exception ->
                        _goals.value =
                            UiState.Error(exception.message ?: "Failed to create goal")
                    }
                )
            }
        }
    }

    fun updateGoal(token: String, goalId: Long, goal: GoalRequestDto, childId: Long) {
        viewModelScope.launch {
            goalsRepository.updateGoal(token, goalId, goal, childId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadGoals(token, childId)
                    },
                    onFailure = { exception ->
                        _goals.value =
                            UiState.Error(exception.message ?: "Failed to update goal")
                    }
                )
            }
        }
    }

    fun deleteGoal(token: String, goalId: Long, childId: Long) {
        viewModelScope.launch {
            goalsRepository.deleteGoal(token, goalId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadGoals(token, childId)
                    },
                    onFailure = { exception ->
                        _goals.value =
                            UiState.Error(exception.message ?: "Failed to delete goal")
                    }
                )
            }
        }
    }

    // Special Needs
    fun loadNeeds(token: String, childId: Long) {
        viewModelScope.launch {
            _needs.value = UiState.Loading

            needsRepository.getNeeds(token, childId).collect { result ->
                result.fold(
                    onSuccess = { needs ->
                        _needs.value = UiState.Success(needs)
                    },
                    onFailure = { exception ->
                        _needs.value =
                            UiState.Error(exception.message ?: "Failed to load notes")
                    }
                )
            }
        }
    }

    fun createNeed(token: String, need: NeedRequestDto, childId: Long) {
        viewModelScope.launch {
            needsRepository.createNeed(token, need, childId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadNeeds(token, childId)
                    },
                    onFailure = { exception ->
                        _needs.value =
                            UiState.Error(exception.message ?: "Failed to create special need")
                    }
                )
            }
        }
    }

    fun updateNeed(token: String, needId: Long, need: NeedRequestDto, childId: Long) {
        viewModelScope.launch {
            needsRepository.updateNeed(token, needId, need, childId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadNeeds(token, childId)
                    },
                    onFailure = { exception ->
                        _needs.value =
                            UiState.Error(exception.message ?: "Failed to update special need")
                    }
                )
            }
        }
    }

    fun deleteNeed(token: String, needId: Long, childId: Long) {
        viewModelScope.launch {
            needsRepository.deleteNeed(token, needId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadNeeds(token, childId)
                    },
                    onFailure = { exception ->
                        _needs.value =
                            UiState.Error(exception.message ?: "Failed to delete special need")
                    }
                )
            }
        }
    }

    //Notes
    fun loadNotes(token: String, childId: Long) {
        viewModelScope.launch {
            _notes.value = UiState.Loading

            notesRepository.getNotes(token, childId).collect { result ->
                result.fold(
                    onSuccess = { notes ->
                        _notes.value = UiState.Success(notes)
                    },
                    onFailure = { exception ->
                        _notes.value =
                            UiState.Error(exception.message ?: "Failed to load notes")
                    }
                )
            }
        }
    }

    fun createNote(token: String, note: NoteRequestDto, childId: Long) {
        viewModelScope.launch {
            notesRepository.createNote(token, note, childId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadNotes(token, childId)
                    },
                    onFailure = { exception ->
                        _notes.value =
                            UiState.Error(exception.message ?: "Failed to create note")
                    }
                )
            }
        }
    }

    fun updateNote(token: String, noteId: Long, note: NoteRequestDto, childId: Long) {
        viewModelScope.launch {
            notesRepository.updateNote(token, noteId, note, childId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadNotes(token, childId)
                    },
                    onFailure = { exception ->
                        _notes.value =
                            UiState.Error(exception.message ?: "Failed to update note")
                    }
                )
            }
        }
    }

    fun deleteNote(token: String, noteId: Long, childId: Long) {
        viewModelScope.launch {
            notesRepository.deleteNote(token, noteId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadNotes(token, childId)
                    },
                    onFailure = { exception ->
                        _notes.value =
                            UiState.Error(exception.message ?: "Failed to delete note")
                    }
                )
            }
        }
    }

    //Monitoring Entries
    fun loadMonitoringEntryData(
        token: String,
        childId: Long
    ) {
        viewModelScope.launch {
            _entries.value = UiState.Loading

            entriesRepository.getMonitoringEntryData(token, childId).collect { result ->
                result.fold(
                    onSuccess = { monitoringData ->
                        _entries.value = UiState.Success(monitoringData)
                    },
                    onFailure = { exception ->
                        _entries.value =
                            UiState.Error(
                                exception.message ?: "Failed to load monitoring data"
                            )
                    }
                )
            }
        }
    }

    fun loadMonitoringEntryByCompanion(
        token: String
    ) {
        viewModelScope.launch {
            _entries.value = UiState.Loading

            entriesRepository.getMonitoringEntryByCompanion(token).collect { result ->
                result.fold(
                    onSuccess = { monitoringData ->
                        _entries.value = UiState.Success(monitoringData)
                    },
                    onFailure = { exception ->
                        _entries.value =
                            UiState.Error(
                                exception.message ?: "Failed to load monitoring data"
                            )
                    }
                )
            }
        }
    }

    fun createMonitoringEntry(
        token: String,
        entry: EntryRequestDto,
        childId: Long,
        paramId: Long
    ) {
        viewModelScope.launch {
            entriesRepository.createMonitoringEntry(token, entry, childId, paramId)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            loadMonitoringEntryData(token, childId)
                        },
                        onFailure = { exception ->
                            _entries.value = UiState.Error(
                                exception.message ?: "Failed to create monitoring data"
                            )
                        }
                    )
                }
        }
    }

    fun updateMonitoringEntry(
        token: String,
        entry: EntryRequestDto,
        childId: Long,
        paramId: Long
    ) {
        viewModelScope.launch {
            entriesRepository.updateMonitoringEntry(token, entry, childId, paramId)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            loadMonitoringEntryData(token, childId)
                        },
                        onFailure = { exception ->
                            _entries.value = UiState.Error(
                                exception.message ?: "Failed to update monitoring data"
                            )
                        }
                    )
                }
        }
    }

    fun deleteMonitoringEntry(
        token: String,
        entryId: Long,
        childId: Long,
    ) {
        viewModelScope.launch {
            entriesRepository.deleteMonitoringEntry(token, entryId, childId)
                .collect { result ->
                    result.fold(
                        onSuccess = {
                            loadMonitoringEntryData(token, childId)
                        },
                        onFailure = { exception ->
                            _entries.value = UiState.Error(
                                exception.message ?: "Failed to delete monitoring data"
                            )
                        }
                    )
                }
        }
    }
}
