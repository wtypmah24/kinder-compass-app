package com.example.school_companion.data.api

import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.Photo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.time.LocalDate

interface ChildrenApi {
    @POST("child")
    suspend fun addChild(
        @Body childDto: ChildDto,
    ): Response<ResponseBody>

    @PATCH("child/{childId}")
    suspend fun updateChild(
        @Body childUpdateDto: ChildDto,
        @Path("childId") childId: Long
    ): Response<ResponseBody>

    @DELETE("child/{childId}")
    suspend fun delete(
        @Path("childId") childId: Long
    ): Response<ResponseBody>

    @GET("child")
    suspend fun getChildren(): Response<List<Child>>

    @GET("child/{childId}")
    suspend fun getChild(
        @Path("childId") childId: Long
    ): Response<Child>

    @GET("child/{childId}/photos")
    suspend fun getChildPhotos(
        @Path("childId") childId: Long
    ): Response<List<Photo>>

    @Multipart
    @POST("child/{childId}/upload")
    suspend fun addChildPhoto(
        @Path("childId") childId: Long,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<ResponseBody>

    @DELETE("child/photo")
    suspend fun deleteChildPhoto(
        @Body deletePhotoRequestDto: DeletePhotoRequestDto
    ): Response<ResponseBody>
}

data class ChildDto(
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val email: String,
    val dateOfBirth: LocalDate
)

data class DeletePhotoRequestDto(
    val photoId: String
)
