package com.example.school_companion.data.repository

import com.example.school_companion.data.api.EntryApi
import com.example.school_companion.data.api.EntryRequestDto
import com.example.school_companion.data.model.MonitoringEntry
import com.example.school_companion.data.model.MonitoringParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Path
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

    suspend fun getMonitoringEntryByCompanion(
        token: String,
    ): Flow<Result<List<MonitoringEntry>>> = flow {
        try {
            val response =
                apiService.getEntriesByCompanion("Bearer $token")
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

    suspend fun createMonitoringEntry(
        token: String,
        monitoringParam: EntryRequestDto,
        childId: Long,
        paramId: Long
    ) = flow {
        try {
            val response = apiService.addEntry("Bearer $token", monitoringParam, childId, paramId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Monitoring entry added successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to create monitoring data: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun updateMonitoringEntry(
        token: String,
        entryUpdateDto: EntryRequestDto,
        entryId: Long,
        childId: Long
    ) = flow {
        try {
            val response =
                apiService.updateEntry("Bearer $token", entryUpdateDto, childId, entryId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Monitoring entry updated successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to update monitoring entry: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun deleteMonitoringEntry(
        token: String,
        entryId: Long,
        childId: Long
    ) = flow {
        try {
            val response =
                apiService.delete("Bearer $token", entryId, childId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Monitoring entry deleted successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to update monitoring entry: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
} 