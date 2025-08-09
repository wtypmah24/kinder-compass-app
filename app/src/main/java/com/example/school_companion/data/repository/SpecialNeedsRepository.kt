package com.example.school_companion.data.repository

import com.example.school_companion.data.api.NeedRequestDto
import com.example.school_companion.data.api.SpecialNeedApi
import com.example.school_companion.data.model.SpecialNeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SpecialNeedsRepository @Inject constructor(
    private val apiService: SpecialNeedApi
) {

    suspend fun getNeeds(
        token: String,
        childId: Long
    ): Flow<Result<List<SpecialNeed>>> = flow {
        try {
            val response = apiService.getNeedsByChild("Bearer $token", childId)
            if (response.isSuccessful) {
                response.body()?.let { needs ->
                    emit(Result.success(needs))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get notes: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun createNeed(token: String, need: NeedRequestDto, childId: Long) = flow {
        try {
            val response = apiService.addNeed("Bearer $token", need, childId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Special need added successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to create special need: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun updateNeed(token: String, needId: Long, need: NeedRequestDto, childId: Long) =
        flow {
            try {
                val response = apiService.updateNeed("Bearer $token", need, childId, needId)
                if (response.isSuccessful) {
                    val message = response.body()?.string() ?: "Special need updated successfully"
                    emit(Result.success(message))
                } else {
                    emit(Result.failure(Exception("Failed to update special need: ${response.code()}")))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }

    suspend fun deleteNeed(token: String, needId: Long) = flow {
        try {
            val response = apiService.delete("Bearer $token", needId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Special need deleted successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to delete special need: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
} 