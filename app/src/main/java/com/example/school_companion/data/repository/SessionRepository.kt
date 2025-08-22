package com.example.school_companion.data.repository

import com.example.school_companion.data.api.SessionApi
import com.example.school_companion.data.api.SessionApi.SessionUpdateDto
import com.example.school_companion.data.model.WorkSession
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val apiService: SessionApi
) {
    suspend fun startSession(
        token: String,
    ) = flow {
        try {
            val response = apiService.start("Bearer $token")
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Work session started successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to start work session: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun endSession(
        token: String,
    ) = flow {
        try {
            val response = apiService.end("Bearer $token")
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Work session ended successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to end work session: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun status(token: String): Flow<Result<WorkSession?>> = flow {
        val gson = Gson()
        try {
            val response = apiService.status("Bearer $token")
            if (response.isSuccessful) {
                val bodyString = response.body()?.string()
                val session = if (bodyString.isNullOrEmpty()) null
                else gson.fromJson(bodyString, WorkSession::class.java)
                emit(Result.success(session))
            } else {
                emit(Result.failure(Exception("Failed to get session: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }


    suspend fun reports(
        token: String,
        startTime: LocalDate,
        endTIme: LocalDate,
    ): Flow<Result<List<WorkSession>>> = flow {
        try {
            val response =
                apiService.report("Bearer $token", startTime.toString(), endTIme.toString())
            if (response.isSuccessful) {
                response.body()?.let { sessions ->
                    emit(Result.success(sessions))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get events: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun update(
        token: String,
        sessionId: Long,
        dto: SessionUpdateDto
    ) = flow {
        try {
            val response = apiService.updateWorkSession("Bearer $token", sessionId, dto)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Work session updated successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to update work session: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun delete(
        token: String,
        sessionId: Long,
    ) = flow {
        try {
            val response = apiService.deleteWorkSession("Bearer $token", sessionId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Work session deleted successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to delete work session: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
} 