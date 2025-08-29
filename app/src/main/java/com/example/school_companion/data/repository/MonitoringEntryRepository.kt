package com.example.school_companion.data.repository

import com.example.school_companion.data.api.EntryApi
import com.example.school_companion.data.api.EntryRequestDto
import com.example.school_companion.data.model.MonitoringEntry
import com.example.school_companion.ui.util.toResult
import com.example.school_companion.ui.util.toResultString
import javax.inject.Inject

class MonitoringEntryRepository @Inject constructor(
    private val apiService: EntryApi
) {
    suspend fun getMonitoringEntryData(childId: Long): Result<List<MonitoringEntry>> {
        return try {
            apiService.getEntriesByChild(childId).toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMonitoringEntryByCompanion(): Result<List<MonitoringEntry>> {
        return try {
            apiService.getEntriesByCompanion().toResult()
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
            apiService.addEntry(monitoringParam, childId, paramId)
                .toResultString("Monitoring entry added successfully")
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
            apiService.updateEntry(entryUpdateDto, childId, entryId)
                .toResultString("Monitoring entry updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteMonitoringEntry(
        entryId: Long,
        childId: Long
    ): Result<String> {
        return try {
            apiService.delete(entryId, childId)
                .toResultString("Monitoring entry deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
