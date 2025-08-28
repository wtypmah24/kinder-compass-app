package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ChildDto
import com.example.school_companion.data.api.ChildrenApi
import com.example.school_companion.data.api.DeletePhotoRequestDto
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
    suspend fun addChild(child: ChildDto): Result<String> {
        return try {
            val response = apiService.addChild(child)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Child added successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to create child: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateChild(childId: Long, child: ChildDto): Result<String> {
        return try {
            val response = apiService.updateChild(child, childId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Child updated successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to update child: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteChild(childId: Long): Result<String> {
        return try {
            val response = apiService.delete(childId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Child deleted successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to delete child: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChildren(): Result<List<Child>> {
        return try {
            val response = apiService.getChildren()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get children: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChild(childId: Long): Result<Child> {
        return try {
            val response = apiService.getChild(childId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get child: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChildPhotos(childId: Long): Result<List<Photo>> {
        return try {
            val response = apiService.getChildPhotos(childId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get child photos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addChildPhotos(childId: Long, file: File, descriptionText: String): Result<String> {
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(
            name = "file",
            filename = file.name,
            body = requestFile
        )
        val descriptionPart = descriptionText.toRequestBody("text/plain".toMediaTypeOrNull())

        return try {
            val response = apiService.addChildPhoto(childId, filePart, descriptionPart)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Photo added successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to add child photo: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteChildPhoto(deletePhotoRequestDto: DeletePhotoRequestDto): Result<String> {
        return try {
            val response = apiService.deleteChildPhoto(deletePhotoRequestDto)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Photo deleted successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to delete child photo: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
