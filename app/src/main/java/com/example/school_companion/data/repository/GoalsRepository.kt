package com.example.school_companion.data.repository

import com.example.school_companion.data.api.GoalApi
import com.example.school_companion.data.api.GoalRequestDto
import com.example.school_companion.data.model.Goal
import com.example.school_companion.ui.util.toResult
import com.example.school_companion.ui.util.toResultString
import javax.inject.Inject

class GoalsRepository @Inject constructor(
    private val apiService: GoalApi
) {
    suspend fun getGoals(childId: Long): Result<List<Goal>> {
        return try {
            apiService.getGoalsByChild(childId).toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createGoal(goal: GoalRequestDto, childId: Long): Result<String> {
        return try {
            apiService.addGoal(goal, childId)
                .toResultString("Goal added successfully.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateGoal(goalId: Long, goal: GoalRequestDto, childId: Long): Result<String> {
        return try {
            apiService.updateGoal(goal, childId, goalId)
                .toResultString("Goal updated successfully.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteGoal(goalId: Long): Result<String> {
        return try {
            apiService.delete(goalId)
                .toResultString("Goal deleted successfully.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
