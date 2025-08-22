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

    suspend fun updateCompanion(
        token: String,
        dto: CompanionUpdateDto
    ) = flow {
        try {
            val response = apiService.update("Bearer $token", dto)
            if (response.isSuccessful) {
                val message = "Companion updated successfully."
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to update companion: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun deleteCompanion(token: String) = flow {
        try {
            val response = apiService.delete("Bearer $token")
            if (response.isSuccessful) {
                val message = "Companion deleted successfully."
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to delete companion: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun getNotificationStatus(token: String): Flow<Result<Boolean>> = flow {
        try {
            val response = apiService.getNotificationStatus("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { enabled ->
                    emit(Result.success(enabled))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to delete companion: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun updateNotificationStatus(token: String, enabled: Boolean) = flow {
        try {
            val response = apiService.updateNotificationStatus("Bearer $token", enabled)
            if (response.isSuccessful) {
                val message = "Companion notifications status updated successfully."
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to update companion notification status: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun updatePassword(token: String, dto: PasswordUpdateDto) = flow {
        try {
            val response = apiService.updatePassword("Bearer $token", dto)
            if (response.isSuccessful) {
                val message = "Companion password updated successfully."
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to update Companion password: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
} 