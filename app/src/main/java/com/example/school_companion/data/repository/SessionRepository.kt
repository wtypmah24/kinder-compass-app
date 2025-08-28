package com.example.school_companion.data.repository

import com.example.school_companion.data.api.SessionApi
import com.example.school_companion.data.api.SessionApi.SessionUpdateDto
import com.example.school_companion.data.model.WorkSession
import com.google.gson.Gson
import java.time.LocalDate
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val apiService: SessionApi
) {
    suspend fun startSession(): Result<String> {
        return try {
            val response = apiService.start()
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Work session started successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to start work session: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun endSession(): Result<String> {
        return try {
            val response = apiService.end()
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Work session ended successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to end work session: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun status(): Result<WorkSession?> {
        return try {
            val gson = Gson()
            val response = apiService.status()
            if (response.isSuccessful) {
                val bodyString = response.body()?.string()
                val session = if (bodyString.isNullOrEmpty()) null
                else gson.fromJson(bodyString, WorkSession::class.java)
                Result.success(session)
            } else {
                Result.failure(Exception("Failed to get session: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun reports(startTime: LocalDate, endTime: LocalDate): Result<List<WorkSession>> {
        return try {
            val response = apiService.report(startTime.toString(), endTime.toString())
            if (response.isSuccessful) {
                response.body()?.let { sessions ->
                    Result.success(sessions)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get reports: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun update(sessionId: Long, dto: SessionUpdateDto): Result<String> {
        return try {
            val response = apiService.updateWorkSession(sessionId, dto)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Work session updated successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to update work session: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun delete(sessionId: Long): Result<String> {
        return try {
            val response = apiService.deleteWorkSession(sessionId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Work session deleted successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to delete work session: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
