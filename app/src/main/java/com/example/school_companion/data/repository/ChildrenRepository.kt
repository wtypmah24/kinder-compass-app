package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ChildRequestDto
import com.example.school_companion.data.api.ChildrenApi
import com.example.school_companion.data.api.DeletePhotoRequestDto
import com.example.school_companion.data.api.EventRequestDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class ChildrenRepository @Inject constructor(
    private val apiService: ChildrenApi
) {

    suspend fun addChild(token: String, child: ChildRequestDto) = flow {
        try {
            val response = apiService.addChild("Bearer $token", child)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Child added successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to create child: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun updateChild(
        token: String,
        childId: Long,
        child: ChildRequestDto
    ) = flow {
        try {
            val response = apiService.updateChild("Bearer $token", child, childId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Child updated successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to update child: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun deleteChild(
        token: String,
        childId: Long
    ) = flow {
        try {
            val response = apiService.delete("Bearer $token", childId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Child deleted successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to delete child: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

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
        val response = apiService.getChild("Bearer $token", childId)
        if (response.isSuccessful) {
            response.body()?.let { child ->
                emit(Result.success(child))
            } ?: emit(Result.failure(Exception("Empty response")))
        } else {
            emit(Result.failure(Exception("Failed to get child: ${response.code()}")))
        }
    }.catch { e ->
        emit(Result.failure(e))
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

    suspend fun addChildPhotos(
        token: String,
        childId: Long,
        file: File,
        descriptionText: String
    ) = flow {
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

        val filePart = MultipartBody.Part.createFormData(
            name = "file",
            filename = file.name,
            body = requestFile
        )
        val descriptionPart = descriptionText.toRequestBody("text/plain".toMediaTypeOrNull())

        try {
            val response =
                apiService.addChildPhoto("Bearer $token", childId, filePart, descriptionPart)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Photo added successfully"
                emit(Result.success(message))
            } else {
                emit(Result.failure(Exception("Failed to add child photo: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun deleteChildPhoto(token: String, deletePhotoRequestDto: DeletePhotoRequestDto) =
        flow {
            try {
                val response = apiService.deleteChildPhoto("Bearer $token", deletePhotoRequestDto)
                if (response.isSuccessful) {
                    val message = response.body()?.string() ?: "Photo deleted successfully"
                    emit(Result.success(message))
                } else {
                    emit(Result.failure(Exception("Failed to delete child photo: ${response.code()}")))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
} 