package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.api.TaskRequestDto
import com.example.school_companion.data.model.Task
import com.example.school_companion.data.repository.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val notesRepository: TasksRepository
) : ViewModel() {

    private val _tasksState = MutableStateFlow<TasksState>(TasksState.Loading)
    val tasksState: StateFlow<TasksState> = _tasksState.asStateFlow()


    fun loadTasks(token: String, childId: Long) {
        viewModelScope.launch {
            _tasksState.value = TasksState.Loading

            notesRepository.getTasks(token, childId).collect { result ->
                result.fold(
                    onSuccess = { notes ->
                        _tasksState.value = TasksState.Success(notes)
                    },
                    onFailure = { exception ->
                        _tasksState.value =
                            TasksState.Error(exception.message ?: "Failed to load tasks")
                    }
                )
            }
        }
    }

    fun createTask(token: String, task: TaskRequestDto, childId: Long) {
        viewModelScope.launch {
            notesRepository.createTask(token, task, childId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadTasks(token, childId)
                    },
                    onFailure = { exception ->
                        _tasksState.value =
                            TasksState.Error(exception.message ?: "Failed to create task")
                    }
                )
            }
        }
    }

    fun updateTask(token: String, taskId: Long, task: TaskRequestDto, childId: Long) {
        viewModelScope.launch {
            notesRepository.updateTask(token, taskId, task, childId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadTasks(token, childId)
                    },
                    onFailure = { exception ->
                        _tasksState.value =
                            TasksState.Error(exception.message ?: "Failed to update task")
                    }
                )
            }
        }
    }

    fun deleteTask(token: String, taskId: Long, childId: Long) {
        viewModelScope.launch {
            notesRepository.deleteTask(token, taskId, childId).collect { result ->
                result.fold(
                    onSuccess = {
                        loadTasks(token, childId)
                    },
                    onFailure = { exception ->
                        _tasksState.value =
                            TasksState.Error(exception.message ?: "Failed to delete task")
                    }
                )
            }
        }
    }
}

sealed class TasksState {
    data object Loading : TasksState()
    data class Success(val tasks: List<Task>) : TasksState()
    data class Error(val message: String) : TasksState()
} 