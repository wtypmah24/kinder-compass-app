package com.example.school_companion.data.repository

import com.example.school_companion.data.api.GoalApi
import com.example.school_companion.data.api.GoalRequestDto
import com.example.school_companion.data.model.Goal
import javax.inject.Inject

class GoalsRepository @Inject constructor(
    private val apiService: GoalApi
) {
    suspend fun getGoals(childId: Long): Result<List<Goal>> {
        return try {
            val response = apiService.getGoalsByChild(childId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get goals: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createGoal(goal: GoalRequestDto, childId: Long): Result<String> {
        return try {
            val response = apiService.addGoal(goal, childId)
            if (response.isSuccessful) {
                Result.success("Goal added successfully.")
            } else {
                Result.failure(Exception("Failed to create goal: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateGoal(goalId: Long, goal: GoalRequestDto, childId: Long): Result<String> {
        return try {
            val response = apiService.updateGoal(goal, childId, goalId)
            if (response.isSuccessful) {
                Result.success("Goal updated successfully.")
            } else {
                Result.failure(Exception("Failed to update goal: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteGoal(goalId: Long): Result<String> {
        return try {
            val response = apiService.delete(goalId)
            if (response.isSuccessful) {
                Result.success("Goal deleted successfully.")
            } else {
                Result.failure(Exception("Failed to delete goal: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
