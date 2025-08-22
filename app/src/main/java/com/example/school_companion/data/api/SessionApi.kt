package com.example.school_companion.data.api

import com.example.school_companion.data.model.WorkSession
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDateTime

interface SessionApi {
    @GET("work-session/start")
    suspend fun start(
        @Header("Authorization") token: String,
    ): Response<ResponseBody>

    @GET("work-session/end")
    suspend fun end(
        @Header("Authorization") token: String,
    ): Response<ResponseBody>

    @GET("work-session/status")
    suspend fun status(
        @Header("Authorization") token: String,
    ): Response<ResponseBody>

    @GET("work-session/reports")
    suspend fun report(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<List<WorkSession>>

    @PATCH("work-session/{sessionId}")
    suspend fun updateWorkSession(
        @Header("Authorization") token: String,
        @Path("sessionId") sessionId: Long,
        @Body sessionUpdateDto: SessionUpdateDto
    ): Response<ResponseBody>

    @DELETE("work-session/{sessionId}")
    suspend fun deleteWorkSession(
        @Header("Authorization") token: String,
        @Path("sessionId") sessionId: Long,
    ): Response<ResponseBody>

    data class SessionUpdateDto(
        val startTime: String,
        val endTime: String,
        val note: String?
    )
}

