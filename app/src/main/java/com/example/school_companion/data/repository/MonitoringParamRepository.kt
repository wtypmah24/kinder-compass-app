package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ParamApi
import com.example.school_companion.data.api.ParamRequestDto
import com.example.school_companion.data.model.MonitoringParam
import javax.inject.Inject

class MonitoringParamRepository @Inject constructor(
    private val apiService: ParamApi
) {
    suspend fun getMonitoringParamData(): Result<List<MonitoringParam>> {
        return try {
            val response = apiService.getParamsByCompanion()
            if (response.isSuccessful) {
                response.body()?.let { monitoringData ->
                    Result.success(monitoringData)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get monitoring param data: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createMonitoringParam(monitoringParam: ParamRequestDto): Result<Unit> {
        return try {
            val response = apiService.addParam(monitoringParam)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to create monitoring param: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateMonitoringParam(
        entryUpdateDto: ParamRequestDto,
        paramId: Long
    ): Result<Unit> {
        return try {
            val response = apiService.updateParam(entryUpdateDto, paramId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update monitoring param: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteMonitoringParam(paramId: Long): Result<Unit> {
        return try {
            val response = apiService.delete(paramId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete monitoring param: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMonitoringParamById(paramId: Long): Result<MonitoringParam> {
        return try {
            val response = apiService.getParamById(paramId)
            if (response.isSuccessful) {
                response.body()?.let { monitoringData ->
                    Result.success(monitoringData)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get monitoring param data: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
