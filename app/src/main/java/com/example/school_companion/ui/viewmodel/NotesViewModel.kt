package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    
    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote: StateFlow<Note?> = _selectedNote.asStateFlow()
    
    fun loadNotes(token: String, childId: String? = null) {
        viewModelScope.launch {
            _notesState.value = NotesState.Loading
            
            notesRepository.getNotes(token, childId).collect { result ->
                result.fold(
                    onSuccess = { notes ->
                        _notesState.value = NotesState.Success(notes)
                    },
                    onFailure = { exception ->
                        _notesState.value = NotesState.Error(exception.message ?: "Failed to load notes")
                    }
                )
            }
        }
    }
    
    fun createNote(token: String, note: Note) {
        viewModelScope.launch {
            notesRepository.createNote(token, note).collect { result ->
                result.fold(
                    onSuccess = { createdNote ->
                        // Refresh notes list
                        loadNotes(token, note.childId)
                    },
                    onFailure = { exception ->
                        _notesState.value = NotesState.Error(exception.message ?: "Failed to create note")
                    }
                )
            }
        }
    }
    
    fun updateNote(token: String, noteId: String, note: Note) {
        viewModelScope.launch {
            notesRepository.updateNote(token, noteId, note).collect { result ->
                result.fold(
                    onSuccess = { updatedNote ->
                        // Refresh notes list
                        loadNotes(token, note.childId)
                    },
                    onFailure = { exception ->
                        _notesState.value = NotesState.Error(exception.message ?: "Failed to update note")
                    }
                )
            }
        }
    }
    
    fun deleteNote(token: String, noteId: String, childId: String) {
        viewModelScope.launch {
            notesRepository.deleteNote(token, noteId).collect { result ->
                result.fold(
                    onSuccess = {
                        // Refresh notes list
                        loadNotes(token, childId)
                    },
                    onFailure = { exception ->
                        _notesState.value = NotesState.Error(exception.message ?: "Failed to delete note")
                    }
                )
            }
        }
    }
    
    fun selectNote(note: Note) {
        _selectedNote.value = note
    }
    
    fun clearSelectedNote() {
        _selectedNote.value = null
    }
}

sealed class NotesState {
    object Loading : NotesState()
    data class Success(val notes: List<Note>) : NotesState()
    data class Error(val message: String) : NotesState()
} 