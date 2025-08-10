package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ParamApi
import com.example.school_companion.data.api.ParamRequestDto
import com.example.school_companion.data.model.MonitoringParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MonitoringParamRepository @Inject constructor(
    private val apiService: ParamApi
) {

    suspend fun getMonitoringParamData(
        token: String,
    ): Flow<Result<List<MonitoringParam>>> = flow {
        try {
            val response =
                apiService.getParamsByCompanion("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { monitoringData ->
                    emit(Result.success(monitoringData))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get monitoring param data: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun createMonitoringParam(
        token: String,
        monitoringParam: ParamRequestDto,
    ) = flow {
        try {
            val response = apiService.addParam("Bearer $token", monitoringParam)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Monitoring param added successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to create monitoring param data: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun updateMonitoringParam(
        token: String,
        entryUpdateDto: ParamRequestDto,
        paramId: Long,
    ) = flow {
        try {
            val response =
                apiService.updateParam("Bearer $token", entryUpdateDto, paramId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Monitoring param updated successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to update monitoring param: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun deleteMonitoringParam(
        token: String,
        paramId: Long,
    ) = flow {
        try {
            val response =
                apiService.delete("Bearer $token", paramId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Monitoring param deleted successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to update monitoring param: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun getMonitoringParamById(
        token: String,
        paramId: Long
    ): Flow<Result<MonitoringParam>> = flow {
        try {
            val response =
                apiService.getParamById("Bearer $token", paramId)
            if (response.isSuccessful) {
                response.body()?.let { monitoringData ->
                    emit(Result.success(monitoringData))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get monitoring param data: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

} 