package com.example.school_companion.data.api

import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.Event
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import java.time.Instant
import java.time.LocalDate

interface EventApi {
    @POST("event/child/{childId}")
    suspend fun addEvent(
        @Header("Authorization") token: String,
        @Body eventRequestDto: EventRequestDto,
        @Path("childId") childId: Long
    ): Response<ResponseBody>

    @PATCH("event/{eventId}/child/{childId}")
    suspend fun updateEvent(
        @Header("Authorization") token: String,
        @Body eventUpdateDto: EventRequestDto,
        @Path("childId") childId: Long,
        @Path("eventId") eventId: Long
    )

    @DELETE("event/{eventId}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: Long
    )

    @GET("event/{eventId}")
    suspend fun getEventById(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: Long
    ): Response<Event>

    @GET("event")
    suspend fun getEventsByCompanion(
        @Header("Authorization") token: String,
    ): Response<List<Event>>

    @GET("event/child/{childId}")
    suspend fun getEventsByChild(
        @Header("Authorization") token: String,
        @Path("childId") childId: Long
    ): Response<List<Event>>
}

data class EventRequestDto(
    val title: String,
    val description: String,
    val startDateTime: String,
    val endDateTime: String,
    val location: String
)


