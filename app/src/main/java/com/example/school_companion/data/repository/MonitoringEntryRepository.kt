package com.example.school_companion.data.repository

import com.example.school_companion.data.api.EntryApi
import com.example.school_companion.data.api.EntryRequestDto
import com.example.school_companion.data.model.MonitoringEntry
import com.example.school_companion.data.model.MonitoringParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MonitoringEntryRepository @Inject constructor(
    private val apiService: EntryApi
) {

    suspend fun getMonitoringEntryData(
        token: String,
        childId: Long
    ): Flow<Result<List<MonitoringEntry>>> = flow {
        try {
            val response =
                apiService.getEntriesByChild("Bearer $token", childId)
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
        monitoringParam: EntryRequestDto,
        childId: Long,
        paramId: Long
    ): Flow<Result<MonitoringEntry>> = flow {
        try {
            val response = apiService.addEntry("Bearer $token", monitoringParam, childId, paramId)
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

//    suspend fun updateMonitoringData(
//        token: String,
//        monitoringId: String,
//        monitoringParam: MonitoringParam
//    ): Flow<Result<MonitoringParam>> = flow {
//        try {
//            val response =
//                apiService.updateMonitoringData("Bearer $token", monitoringId, monitoringParam)
//            if (response.isSuccessful) {
//                response.body()?.let { updatedData ->
//                    emit(Result.success(updatedData))
//                } ?: emit(Result.failure(Exception("Empty response")))
//            } else {
//                emit(Result.failure(Exception("Failed to update monitoring data: ${response.code()}")))
//            }
//        } catch (e: Exception) {
//            emit(Result.failure(e))
//        }
//    }
} 