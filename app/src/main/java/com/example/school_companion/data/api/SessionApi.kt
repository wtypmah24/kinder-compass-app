package com.example.school_companion.data.api

import com.example.school_companion.data.model.WorkSession
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface SessionApi {
    @GET("work-session/start")
    suspend fun start(
    ): Response<ResponseBody>

    @GET("work-session/end")
    suspend fun end(
    ): Response<ResponseBody>

    @GET("work-session/status")
    suspend fun status(
    ): Response<ResponseBody>

    @GET("work-session/reports")
    suspend fun report(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<List<WorkSession>>

    @PATCH("work-session/{sessionId}")
    suspend fun updateWorkSession(
        @Path("sessionId") sessionId: Long,
        @Body sessionUpdateDto: SessionUpdateDto
    ): Response<ResponseBody>

    @DELETE("work-session/{sessionId}")
    suspend fun deleteWorkSession(
        @Path("sessionId") sessionId: Long,
    ): Response<ResponseBody>

    data class SessionUpdateDto(
        val startTime: String,
        val endTime: String,
        val note: String?
    )
}
