package com.example.school_companion.data.repository

import com.example.school_companion.data.api.SessionApi
import com.example.school_companion.data.api.SessionApi.SessionUpdateDto
import com.example.school_companion.data.model.WorkSession
import com.example.school_companion.ui.util.toResult
import com.example.school_companion.ui.util.toResultString
import com.google.gson.Gson
import java.time.LocalDate
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val apiService: SessionApi
) {
    suspend fun startSession(): Result<String> {
        return try {
            apiService.start()
                .toResultString("Work session started successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun endSession(): Result<String> {
        return try {
            apiService.end()
                .toResultString("Work session ended successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun status(): Result<WorkSession?> {
        return try {
            val response = apiService.status()
            if (response.isSuccessful) {
                val bodyString = response.body()?.string()
                val session = bodyString?.let { Gson().fromJson(it, WorkSession::class.java) }
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
            apiService.report(startTime.toString(), endTime.toString()).toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun update(sessionId: Long, dto: SessionUpdateDto): Result<String> {
        return try {
            apiService.updateWorkSession(sessionId, dto)
                .toResultString("Work session updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun delete(sessionId: Long): Result<String> {
        return try {
            apiService.deleteWorkSession(sessionId)
                .toResultString("Work session deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
