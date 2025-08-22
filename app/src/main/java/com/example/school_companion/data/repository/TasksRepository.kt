package com.example.school_companion.data.repository

import com.example.school_companion.data.api.TaskApi
import com.example.school_companion.data.api.TaskRequestDto
import com.example.school_companion.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TasksRepository @Inject constructor(
    private val apiService: TaskApi
) {

    suspend fun getTasks(
        token: String,
        childId: Long
    ): Flow<Result<List<Task>>> = flow {
        try {
            val response = apiService.getTasksByChild("Bearer $token", childId)
            if (response.isSuccessful) {
                response.body()?.let { notes ->
                    emit(Result.success(notes))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get notes: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun createTask(token: String, task: TaskRequestDto, childId: Long) =
        flow {
            try {
                val response = apiService.addTask("Bearer $token", task, childId)
                if (response.isSuccessful) {
                    val message = response.body()?.string() ?: "Task added successfully"
                    emit(Result.success(message))
                } else {
                    emit(Result.failure(Exception("Failed to create task: ${response.code()}")))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }

    suspend fun updateTask(
        token: String,
        noteId: Long,
        task: TaskRequestDto,
        childId: Long
    ) = flow {
        try {
            val response = apiService.updateTask("Bearer $token", task, childId, noteId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Task updated successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to update Task: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun deleteTask(token: String, taskId: Long, childId: Long) = flow {
        try {
            val response = apiService.delete("Bearer $token", taskId, childId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Task deleted successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to delete Task: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
} 