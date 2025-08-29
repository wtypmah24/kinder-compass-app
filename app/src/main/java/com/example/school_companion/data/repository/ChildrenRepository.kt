package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ChildDto
import com.example.school_companion.data.api.ChildrenApi
import com.example.school_companion.data.api.DeletePhotoRequestDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.Photo
import com.example.school_companion.ui.util.toResult
import com.example.school_companion.ui.util.toResultString
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
            apiService.addChild(child)
                .toResultString("Child added successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateChild(childId: Long, child: ChildDto): Result<String> {
        return try {
            apiService.updateChild(child, childId)
                .toResultString("Child updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteChild(childId: Long): Result<String> {
        return try {
            apiService.delete(childId)
                .toResultString("Child deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChildren(): Result<List<Child>> {
        return try {
            apiService.getChildren().toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChild(childId: Long): Result<Child> {
        return try {
            apiService.getChild(childId).toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChildPhotos(childId: Long): Result<List<Photo>> {
        return try {
            apiService.getChildPhotos(childId).toResult()
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
            apiService.addChildPhoto(childId, filePart, descriptionPart)
                .toResultString("Photo added successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteChildPhoto(deletePhotoRequestDto: DeletePhotoRequestDto): Result<String> {
        return try {
            apiService.deleteChildPhoto(deletePhotoRequestDto)
                .toResultString("Photo deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
