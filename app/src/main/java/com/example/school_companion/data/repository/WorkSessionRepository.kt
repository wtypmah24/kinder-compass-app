package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ApiService
import com.example.school_companion.data.api.StatisticsResponse
import com.example.school_companion.data.model.WorkSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WorkSessionRepository @Inject constructor(
    private val apiService: ApiService
) {
    
    suspend fun getWorkSessions(
        token: String,
        startDate: String? = null,
        endDate: String? = null
    ): Flow<Result<List<WorkSession>>> = flow {
        try {
            val response = apiService.getWorkSessions("Bearer $token", startDate, endDate)
            if (response.isSuccessful) {
                response.body()?.let { sessions ->
                    emit(Result.success(sessions))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get work sessions: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun startWorkSession(token: String): Flow<Result<WorkSession>> = flow {
        try {
            val response = apiService.startWorkSession("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { session ->
                    emit(Result.success(session))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to start work session: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun endWorkSession(token: String, sessionId: String): Flow<Result<WorkSession>> = flow {
        try {
            val response = apiService.endWorkSession("Bearer $token", sessionId)
            if (response.isSuccessful) {
                response.body()?.let { session ->
                    emit(Result.success(session))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to end work session: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun getStatistics(
        token: String,
        childId: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): Flow<Result<StatisticsResponse>> = flow {
        try {
            val response = apiService.getStatistics("Bearer $token", childId, startDate, endDate)
            if (response.isSuccessful) {
                response.body()?.let { statistics ->
                    emit(Result.success(statistics))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get statistics: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
} 