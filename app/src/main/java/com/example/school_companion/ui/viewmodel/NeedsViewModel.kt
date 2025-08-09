package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.api.NeedRequestDto
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

    fun createNeed(token: String, need: NeedRequestDto, childId: Long) {
        viewModelScope.launch {
            needsRepository.createNeed(token, need, childId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadNeeds(token, childId)
                    },
                    onFailure = { exception ->
                        _needsState.value =
                            NeedsState.Error(exception.message ?: "Failed to create special need")
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
                        _needsState.value =
                            NeedsState.Error(exception.message ?: "Failed to update special need")
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
                        _needsState.value =
                            NeedsState.Error(exception.message ?: "Failed to delete special need")
                    }
                )
            }
        }
    }

    fun selectGoal(goal: Goal) {
        _selectedNeed.value = goal
    }

    fun clearSelectedGoal() {
        _selectedNeed.value = null
    }
}

sealed class NeedsState {
    data object Loading : NeedsState()
    data class Success(val notes: List<SpecialNeed>) : NeedsState()
    data class Error(val message: String) : NeedsState()
} 