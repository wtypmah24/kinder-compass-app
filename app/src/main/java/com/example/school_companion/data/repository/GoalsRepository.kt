package com.example.school_companion.data.repository

import com.example.school_companion.data.api.GoalApi
import com.example.school_companion.data.api.GoalRequestDto
import com.example.school_companion.data.model.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GoalsRepository @Inject constructor(
    private val apiService: GoalApi
) {
    suspend fun getGoals(
        token: String,
        childId: Long
    ): Flow<Result<List<Goal>>> = flow {
        try {
            val response = apiService.getGoalsByChild("Bearer $token", childId)
            if (response.isSuccessful) {
                response.body()?.let { goals ->
                    emit(Result.success(goals))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get notes: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun createGoal(token: String, goal: GoalRequestDto, childId: Long) = flow {
        try {
            val response = apiService.addGoal("Bearer $token", goal, childId)
            if (response.isSuccessful) {
                val message = "Goal added successfully."
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to create goal: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun updateGoal(token: String, goalId: Long, goal: GoalRequestDto, childId: Long) =
        flow {
            try {
                val response = apiService.updateGoal("Bearer $token", goal, childId, goalId)
                if (response.isSuccessful) {
                    val message = "Goal updated successfully."
                    emit(Result.success(message))
                } else {
                    emit(Result.failure(Exception("Failed to update goal: ${response.code()}")))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }

    suspend fun deleteGoal(token: String, goalId: Long) = flow {
        try {
            val response = apiService.delete("Bearer $token", goalId)
            if (response.isSuccessful) {
                val message = "Goal deleted successfully."
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to delete note: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
} 