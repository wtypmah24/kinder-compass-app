package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ApiService
import com.example.school_companion.data.model.MonitoringData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MonitoringRepository @Inject constructor(
    private val apiService: ApiService
) {
    
    suspend fun getMonitoringData(
        token: String,
        childId: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): Flow<Result<List<MonitoringData>>> = flow {
        try {
            val response = apiService.getMonitoringData("Bearer $token", childId, startDate, endDate)
            if (response.isSuccessful) {
                response.body()?.let { monitoringData ->
                    emit(Result.success(monitoringData))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get monitoring data: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun createMonitoringData(
        token: String,
        monitoringData: MonitoringData
    ): Flow<Result<MonitoringData>> = flow {
        try {
            val response = apiService.createMonitoringData("Bearer $token", monitoringData)
            if (response.isSuccessful) {
                response.body()?.let { createdData ->
                    emit(Result.success(createdData))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to create monitoring data: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    suspend fun updateMonitoringData(
        token: String,
        monitoringId: String,
        monitoringData: MonitoringData
    ): Flow<Result<MonitoringData>> = flow {
        try {
            val response = apiService.updateMonitoringData("Bearer $token", monitoringId, monitoringData)
            if (response.isSuccessful) {
                response.body()?.let { updatedData ->
                    emit(Result.success(updatedData))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to update monitoring data: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
} 