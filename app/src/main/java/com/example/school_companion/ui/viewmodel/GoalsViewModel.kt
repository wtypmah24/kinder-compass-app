package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.api.GoalRequestDto
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

    fun createGoal(token: String, goal: GoalRequestDto, childId: Long) {
        viewModelScope.launch {
            goalsRepository.createGoal(token, goal, childId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadGoals(token, childId)
                    },
                    onFailure = { exception ->
                        _goalsState.value =
                            GoalsState.Error(exception.message ?: "Failed to create goal")
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
                        _goalsState.value =
                            GoalsState.Error(exception.message ?: "Failed to update goal")
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
                        _goalsState.value =
                            GoalsState.Error(exception.message ?: "Failed to delete goal")
                    }
                )
            }
        }
    }
}
sealed class GoalsState {
    object Loading : GoalsState()
    data class Success(val goals: List<Goal>) : GoalsState()
    data class Error(val message: String) : GoalsState()
} 