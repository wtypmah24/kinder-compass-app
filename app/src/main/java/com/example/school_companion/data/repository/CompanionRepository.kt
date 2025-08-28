package com.example.school_companion.data.repository

import com.example.school_companion.data.api.CompanionApi
import com.example.school_companion.data.api.CompanionUpdateDto
import com.example.school_companion.data.api.GoalApi
import com.example.school_companion.data.api.GoalRequestDto
import com.example.school_companion.data.api.PasswordUpdateDto
import com.example.school_companion.data.model.Event
import com.example.school_companion.data.model.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PATCH
import javax.inject.Inject

class CompanionRepository @Inject constructor(
    private val apiService: CompanionApi
) {
    suspend fun updateCompanion(dto: CompanionUpdateDto): Result<String> {
        return try {
            val response = apiService.update(dto)
            if (response.isSuccessful) {
                Result.success("Companion updated successfully.")
            } else {
                Result.failure(Exception("Failed to update companion: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteCompanion(): Result<String> {
        return try {
            val response = apiService.delete()
            if (response.isSuccessful) {
                Result.success("Companion deleted successfully.")
            } else {
                Result.failure(Exception("Failed to delete companion: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getNotificationStatus(): Result<Boolean> {
        return try {
            val response = apiService.getNotificationStatus()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get notification status: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateNotificationStatus(enabled: Boolean): Result<String> {
        return try {
            val response = apiService.updateNotificationStatus(enabled)
            if (response.isSuccessful) {
                Result.success("Companion notifications status updated successfully.")
            } else {
                Result.failure(Exception("Failed to update companion notification status: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePassword(dto: PasswordUpdateDto): Result<String> {
        return try {
            val response = apiService.updatePassword(dto)
            if (response.isSuccessful) {
                Result.success("Companion password updated successfully.")
            } else {
                Result.failure(Exception("Failed to update Companion password: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
