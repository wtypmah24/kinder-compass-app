package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.model.Goal
import com.example.school_companion.data.repository.GoalsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalsRepository: GoalsRepository
) : ViewModel() {

    private val _goalsState = MutableStateFlow<GoalsState>(GoalsState.Loading)
    val goalsState: StateFlow<GoalsState> = _goalsState.asStateFlow()

    private val _selectedGoal = MutableStateFlow<Goal?>(null)
    val selectedGoal: StateFlow<Goal?> = _selectedGoal.asStateFlow()

    fun loadGoals(token: String, childId: Long) {
        viewModelScope.launch {
            _goalsState.value = GoalsState.Loading

            goalsRepository.getGoals(token, childId).collect { result ->
                result.fold(
                    onSuccess = { notes ->
                        _goalsState.value = GoalsState.Success(notes)
                    },
                    onFailure = { exception ->
                        _goalsState.value =
                            GoalsState.Error(exception.message ?: "Failed to load notes")
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
        _selectedGoal.value = goal
    }

    fun clearSelectedGoal() {
        _selectedGoal.value = null
    }
}

sealed class GoalsState {
    object Loading : GoalsState()
    data class Success(val notes: List<Goal>) : GoalsState()
    data class Error(val message: String) : GoalsState()
} 