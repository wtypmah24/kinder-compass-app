package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.model.Goal
import com.example.school_companion.data.model.SpecialNeed
import com.example.school_companion.data.repository.SpecialNeedsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NeedsViewModel @Inject constructor(
    private val needsRepository: SpecialNeedsRepository
) : ViewModel() {

    private val _needsState = MutableStateFlow<NeedsState>(NeedsState.Loading)
    val needsState: StateFlow<NeedsState> = _needsState.asStateFlow()

    private val _selectedNeed = MutableStateFlow<Goal?>(null)
    val selectedGoal: StateFlow<Goal?> = _selectedNeed.asStateFlow()

    fun loadNeeds(token: String, childId: Long) {
        viewModelScope.launch {
            _needsState.value = NeedsState.Loading

            needsRepository.getNeeds(token, childId).collect { result ->
                result.fold(
                    onSuccess = { needs ->
                        _needsState.value = NeedsState.Success(needs)
                    },
                    onFailure = { exception ->
                        _needsState.value =
                            NeedsState.Error(exception.message ?: "Failed to load notes")
                    }
                )
            }
        }
    }

//    fun createNote(token: String, note: Note) {
//        viewModelScope.launch {
//            notesRepository.createNote(token, note).collect { result ->
//                result.fold(
//                    onSuccess = { createdNote ->
//                        // Refresh notes list
//                        loadNotes(token, note.childId)
//                    },
//                    onFailure = { exception ->
//                        _notesState.value = NotesState.Error(exception.message ?: "Failed to create note")
//                    }
//                )
//            }
//        }
//    }

//    fun updateNote(token: String, noteId: String, note: Note) {
//        viewModelScope.launch {
//            notesRepository.updateNote(token, noteId, note).collect { result ->
//                result.fold(
//                    onSuccess = { updatedNote ->
//                        // Refresh notes list
//                        loadNotes(token, note.childId)
//                    },
//                    onFailure = { exception ->
//                        _notesState.value = NotesState.Error(exception.message ?: "Failed to update note")
//                    }
//                )
//            }
//        }
//    }

//    fun deleteNote(token: String, noteId: String, childId: String) {
//        viewModelScope.launch {
//            notesRepository.deleteNote(token, noteId).collect { result ->
//                result.fold(
//                    onSuccess = {
//                        // Refresh notes list
//                        loadNotes(token, childId)
//                    },
//                    onFailure = { exception ->
//                        _notesState.value = NotesState.Error(exception.message ?: "Failed to delete note")
//                    }
//                )
//            }
//        }
//    }

    fun selectGoal(goal: Goal) {
        _selectedNeed.value = goal
    }

    fun clearSelectedGoal() {
        _selectedNeed.value = null
    }
}

sealed class NeedsState {
    object Loading : NeedsState()
    data class Success(val notes: List<SpecialNeed>) : NeedsState()
    data class Error(val message: String) : NeedsState()
} 