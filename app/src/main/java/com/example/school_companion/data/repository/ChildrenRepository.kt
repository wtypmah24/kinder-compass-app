package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ApiService
import com.example.school_companion.data.api.ChildrenApi
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChildrenRepository @Inject constructor(
    private val apiService: ChildrenApi
) {

    suspend fun getChildren(token: String): Flow<Result<List<Child>>> = flow {
        try {
            val response = apiService.getChildren("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { children ->
                    emit(Result.success(children))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get children: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun getChild(token: String, childId: Long): Flow<Result<Child>> = flow {
        try {
            val response = apiService.getChild("Bearer $token", childId)
            if (response.isSuccessful) {
                response.body()?.let { child ->
                    emit(Result.success(child))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get child: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun getChildPhotos(token: String, childId: Long): Flow<Result<List<Photo>>> = flow {
        try {
            val response = apiService.getChildPhotos("Bearer $token", childId)
            if (response.isSuccessful) {
                response.body()?.let { photos ->
                    emit(Result.success(photos))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get child photos: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
} 