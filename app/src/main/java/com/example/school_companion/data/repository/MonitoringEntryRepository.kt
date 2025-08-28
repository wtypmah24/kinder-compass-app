package com.example.school_companion.data.repository

import com.example.school_companion.data.api.EntryApi
import com.example.school_companion.data.api.EntryRequestDto
import com.example.school_companion.data.model.MonitoringEntry
import javax.inject.Inject

class MonitoringEntryRepository @Inject constructor(
    private val apiService: EntryApi
) {
    suspend fun getMonitoringEntryData(childId: Long): Result<List<MonitoringEntry>> {
        return try {
            val response = apiService.getEntriesByChild(childId)
            if (response.isSuccessful) {
                response.body()?.let { monitoringData ->
                    Result.success(monitoringData)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get monitoring data: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMonitoringEntryByCompanion(): Result<List<MonitoringEntry>> {
        return try {
            val response = apiService.getEntriesByCompanion()
            if (response.isSuccessful) {
                response.body()?.let { monitoringData ->
                    Result.success(monitoringData)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get monitoring data: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createMonitoringEntry(
        monitoringParam: EntryRequestDto,
        childId: Long,
        paramId: Long
    ): Result<String> {
        return try {
            val response = apiService.addEntry(monitoringParam, childId, paramId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Monitoring entry added successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to create monitoring entry: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateMonitoringEntry(
        entryUpdateDto: EntryRequestDto,
        entryId: Long,
        childId: Long
    ): Result<String> {
        return try {
            val response = apiService.updateEntry(entryUpdateDto, childId, entryId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Monitoring entry updated successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to update monitoring entry: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteMonitoringEntry(
        entryId: Long,
        childId: Long
    ): Result<String> {
        return try {
            val response = apiService.delete(entryId, childId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Monitoring entry deleted successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to delete monitoring entry: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
