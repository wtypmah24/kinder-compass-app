package com.example.school_companion.data.repository

import com.example.school_companion.data.api.TaskApi
import com.example.school_companion.data.api.TaskRequestDto
import com.example.school_companion.data.model.Task
import javax.inject.Inject

class TasksRepository @Inject constructor(
    private val apiService: TaskApi
) {
    suspend fun getTasks(childId: Long): Result<List<Task>> {
        return try {
            val response = apiService.getTasksByChild(childId)
            if (response.isSuccessful) {
                val tasks = response.body() ?: emptyList()
                Result.success(tasks)
            } else {
                Result.failure(Exception("Failed to get tasks: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTask(task: TaskRequestDto, childId: Long): Result<String> {
        return try {
            val response = apiService.addTask(task, childId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Task added successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to create task: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTask(taskId: Long, task: TaskRequestDto, childId: Long): Result<String> {
        return try {
            val response = apiService.updateTask(task, childId, taskId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Task updated successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to update task: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTask(taskId: Long, childId: Long): Result<String> {
        return try {
            val response = apiService.delete(taskId, childId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Task deleted successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to delete task: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
