package com.example.school_companion.data.repository

import com.example.school_companion.data.api.TaskApi
import com.example.school_companion.data.api.TaskRequestDto
import com.example.school_companion.data.model.Task
import com.example.school_companion.ui.util.toResult
import com.example.school_companion.ui.util.toResultString
import javax.inject.Inject

class TasksRepository @Inject constructor(
    private val apiService: TaskApi
) {
    suspend fun getTasks(childId: Long): Result<List<Task>> {
        return try {
            apiService.getTasksByChild(childId).toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTask(task: TaskRequestDto, childId: Long): Result<String> {
        return try {
            apiService.addTask(task, childId)
                .toResultString("Task added successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTask(taskId: Long, task: TaskRequestDto, childId: Long): Result<String> {
        return try {
            apiService.updateTask(task, childId, taskId)
                .toResultString("Task updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTask(taskId: Long, childId: Long): Result<String> {
        return try {
            apiService.delete(taskId, childId)
                .toResultString("Task deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
