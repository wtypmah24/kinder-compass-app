package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ParamApi
import com.example.school_companion.data.api.ParamRequestDto
import com.example.school_companion.data.model.MonitoringParam
import com.example.school_companion.ui.util.toResult
import com.example.school_companion.ui.util.toResultString
import javax.inject.Inject

class MonitoringParamRepository @Inject constructor(
    private val apiService: ParamApi
) {
    suspend fun getMonitoringParamData(): Result<List<MonitoringParam>> {
        return try {
            apiService.getParamsByCompanion().toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMonitoringParamById(paramId: Long): Result<MonitoringParam> {
        return try {
            apiService.getParamById(paramId).toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createMonitoringParam(monitoringParam: ParamRequestDto): Result<Unit> {
        return try {
            apiService.addParam(monitoringParam)
                .toResultString("Monitoring param created successfully")
                .map { }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateMonitoringParam(
        entryUpdateDto: ParamRequestDto,
        paramId: Long
    ): Result<Unit> {
        return try {
            apiService.updateParam(entryUpdateDto, paramId)
                .toResultString("Monitoring param updated successfully")
                .map { }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteMonitoringParam(paramId: Long): Result<Unit> {
        return try {
            apiService.delete(paramId)
                .toResultString("Monitoring param deleted successfully")
                .map { }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
