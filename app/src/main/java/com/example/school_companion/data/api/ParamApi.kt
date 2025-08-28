package com.example.school_companion.data.api

import com.example.school_companion.data.model.MonitoringParam
import com.example.school_companion.data.model.ScaleType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ParamApi {
    @POST("param")
    suspend fun addParam(
        @Body paramRequestDto: ParamRequestDto,
    ): Response<ResponseBody>

    @PATCH("param/{paramId}")
    suspend fun updateParam(
        @Body paramRequestDto: ParamRequestDto,
        @Path("paramId") paramId: Long
    ): Response<ResponseBody>

    @DELETE("param/{paramId}")
    suspend fun delete(
        @Path("paramId") paramId: Long
    ): Response<ResponseBody>

    @GET("param/{paramId}")
    suspend fun getParamById(
        @Path("paramId") paramId: Long
    ): Response<MonitoringParam>

    @GET("param")
    suspend fun getParamsByCompanion(
    ): Response<List<MonitoringParam>>
}

data class ParamRequestDto(
    val title: String,
    val type: ScaleType,
    val description: String,
    val minValue: Int,
    val maxValue: Int,
)


