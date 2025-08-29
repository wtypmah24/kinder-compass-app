package com.example.school_companion.data.repository

import com.example.school_companion.data.api.CompanionApi
import com.example.school_companion.data.api.CompanionUpdateDto
import com.example.school_companion.data.api.PasswordUpdateDto
import com.example.school_companion.ui.util.toResult
import com.example.school_companion.ui.util.toResultString
import javax.inject.Inject

class CompanionRepository @Inject constructor(
    private val apiService: CompanionApi
) {
    suspend fun updateCompanion(dto: CompanionUpdateDto): Result<String> {
        return try {
            apiService.update(dto)
                .toResultString("Companion updated successfully.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteCompanion(): Result<String> {
        return try {
            apiService.delete()
                .toResultString("Companion deleted successfully.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateNotificationStatus(enabled: Boolean): Result<String> {
        return try {
            apiService.updateNotificationStatus(enabled)
                .toResultString("Companion notifications status updated successfully.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePassword(dto: PasswordUpdateDto): Result<String> {
        return try {
            apiService.updatePassword(dto)
                .toResultString("Companion password updated successfully.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getNotificationStatus(): Result<Boolean> {
        return try {
            apiService.getNotificationStatus().toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
