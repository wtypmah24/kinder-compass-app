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
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.time.LocalDate

interface ChildrenApi {
    @POST("child")
    suspend fun addChild(
        @Header("Authorization") token: String,
        @Body childRequestDto: ChildRequestDto,
    ): Response<ResponseBody>

    @PATCH("child/{childId}")
    suspend fun updateChild(
        @Header("Authorization") token: String,
        @Body childUpdateDto: ChildRequestDto,
        @Path("childId") childId: Long
    ): Response<ResponseBody>

    @DELETE("child/{childId}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("childId") childId: Long
    ): Response<ResponseBody>

    @GET("child")
    suspend fun getChildren(@Header("Authorization") token: String): Response<List<Child>>

    @GET("child/{childId}")
    suspend fun getChild(
        @Header("Authorization") token: String,
        @Path("childId") childId: Long
    ): Response<Child>

    @GET("child/{childId}/photos")
    suspend fun getChildPhotos(
        @Header("Authorization") token: String,
        @Path("childId") childId: Long
    ): Response<List<Photo>>

    @Multipart
    @POST("child/{childId}/upload")
    suspend fun addChildPhoto(
        @Header("Authorization") token: String,
        @Path("childId") childId: Long,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<ResponseBody>

    @DELETE("child/photo")
    suspend fun deleteChildPhoto(
        @Header("Authorization") token: String,
        @Body deletePhotoRequestDto: DeletePhotoRequestDto
    ): Response<ResponseBody>
}

data class ChildRequestDto(
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val email: String,
    val dateOfBirth: LocalDate
)

data class ChildUpdateDto(
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val dateOfBirth: LocalDate,
    val active: Boolean,
)

data class DeletePhotoRequestDto(
    val photoId: String
)
