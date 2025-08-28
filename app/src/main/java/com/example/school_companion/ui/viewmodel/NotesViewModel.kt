package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.api.NoteRequestDto
import com.example.school_companion.data.model.Note
import com.example.school_companion.data.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _notesState = MutableStateFlow<NotesState>(NotesState.Loading)
    val notesState: StateFlow<NotesState> = _notesState.asStateFlow()

    fun loadNotes(childId: Long) {
        viewModelScope.launch {
            _notesState.value = NotesState.Loading
            val result = notesRepository.getNotes(childId)
            result.fold(
                onSuccess = { notes ->
                    _notesState.value = NotesState.Success(notes)
                },
                onFailure = { exception ->
                    _notesState.value = NotesState.Error(
                        exception.message ?: "Failed to load notes"
                    )
                }
            )
        }
    }

    fun createNote(note: NoteRequestDto, childId: Long) {
        viewModelScope.launch {
            val result = notesRepository.createNote(note, childId)
            result.fold(
                onSuccess = {
                    loadNotes(childId)
                },
                onFailure = { exception ->
                    _notesState.value = NotesState.Error(
                        exception.message ?: "Failed to create note"
                    )
                }
            )
        }
    }

    fun updateNote(noteId: Long, note: NoteRequestDto, childId: Long) {
        viewModelScope.launch {
            val result = notesRepository.updateNote(noteId, note, childId)
            result.fold(
                onSuccess = {
                    loadNotes(childId)
                },
                onFailure = { exception ->
                    _notesState.value = NotesState.Error(
                        exception.message ?: "Failed to update note"
                    )
                }
            )
        }
    }

    fun deleteNote(noteId: Long, childId: Long) {
        viewModelScope.launch {
            val result = notesRepository.deleteNote(noteId)
            result.fold(
                onSuccess = {
                    loadNotes(childId)
                },
                onFailure = { exception ->
                    _notesState.value = NotesState.Error(
                        exception.message ?: "Failed to delete note"
                    )
                }
            )
        }
    }
}

sealed class NotesState {
    data object Loading : NotesState()
    data class Success(val notes: List<Note>) : NotesState()
    data class Error(val message: String) : NotesState()
}
