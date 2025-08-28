package com.example.school_companion.data.api

import com.example.school_companion.data.model.Event
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface EventApi {
    @POST("event/child/{childId}")
    suspend fun addEvent(
        @Body eventRequestDto: EventRequestDto,
        @Path("childId") childId: Long
    ): Response<ResponseBody>

    @PATCH("event/{eventId}/child/{childId}")
    suspend fun updateEvent(
        @Body eventUpdateDto: EventRequestDto,
        @Path("childId") childId: Long,
        @Path("eventId") eventId: Long
    ): Response<ResponseBody>

    @DELETE("event/{eventId}/child/{childId}")
    suspend fun delete(
        @Path("eventId") eventId: Long,
        @Path("childId") childId: Long
    ): Response<ResponseBody>

    @GET("event/{eventId}")
    suspend fun getEventById(
        @Path("eventId") eventId: Long
    ): Response<Event>

    @GET("event")
    suspend fun getEventsByCompanion(
    ): Response<List<Event>>

    @GET("event/child/{childId}")
    suspend fun getEventsByChild(
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
