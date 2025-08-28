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
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _tasksState = MutableStateFlow<TasksState>(TasksState.Loading)
    val tasksState: StateFlow<TasksState> = _tasksState.asStateFlow()

    fun loadTasks(childId: Long) {
        viewModelScope.launch {
            _tasksState.value = TasksState.Loading
            val result = tasksRepository.getTasks(childId)
            result.fold(
                onSuccess = { tasks ->
                    _tasksState.value = TasksState.Success(tasks)
                },
                onFailure = { exception ->
                    _tasksState.value =
                        TasksState.Error(exception.message ?: "Failed to load tasks")
                }
            )
        }
    }

    fun createTask(task: TaskRequestDto, childId: Long) {
        viewModelScope.launch {
            val result = tasksRepository.createTask(task, childId)
            result.fold(
                onSuccess = {
                    loadTasks(childId)
                },
                onFailure = { exception ->
                    _tasksState.value =
                        TasksState.Error(exception.message ?: "Failed to create task")
                }
            )
        }
    }

    fun updateTask(taskId: Long, task: TaskRequestDto, childId: Long) {
        viewModelScope.launch {
            val result = tasksRepository.updateTask(taskId, task, childId)
            result.fold(
                onSuccess = {
                    loadTasks(childId)
                },
                onFailure = { exception ->
                    _tasksState.value =
                        TasksState.Error(exception.message ?: "Failed to update task")
                }
            )
        }
    }

    fun deleteTask(taskId: Long, childId: Long) {
        viewModelScope.launch {
            val result = tasksRepository.deleteTask(taskId, childId)
            result.fold(
                onSuccess = {
                    loadTasks(childId)
                },
                onFailure = { exception ->
                    _tasksState.value =
                        TasksState.Error(exception.message ?: "Failed to delete task")
                }
            )
        }
    }
}

sealed class TasksState {
    data object Loading : TasksState()
    data class Success(val tasks: List<Task>) : TasksState()
    data class Error(val message: String) : TasksState()
}
