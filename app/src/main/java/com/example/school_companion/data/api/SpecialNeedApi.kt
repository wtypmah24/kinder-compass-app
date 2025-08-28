package com.example.school_companion.data.api

import com.example.school_companion.data.model.SpecialNeed
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface SpecialNeedApi {
    @POST("note/child/{childId}")
    suspend fun addNeed(
        @Body needRequestDto: NeedRequestDto,
        @Path("childId") childId: Long
    ): Response<ResponseBody>

    @PATCH("need/{needId}/child/{childId}")
    suspend fun updateNeed(
        @Body needUpdateDto: NeedRequestDto,
        @Path("childId") childId: Long,
        @Path("needId") needId: Long
    ): Response<ResponseBody>

    @DELETE("need/{needId}")
    suspend fun delete(
        @Path("needId") needId: Long
    ): Response<ResponseBody>

    @GET("need/{needId}")
    suspend fun getNeedById(
        @Path("needId") noteId: Long
    ): Response<SpecialNeed>

    @GET("need")
    suspend fun getNeedsByCompanion(
    ): Response<List<SpecialNeed>>

    @GET("need/child/{childId}")
    suspend fun getNeedsByChild(
        @Path("childId") childId: Long
    ): Response<List<SpecialNeed>>
}

data class NeedRequestDto(
    val type: String,
    val description: String
)
