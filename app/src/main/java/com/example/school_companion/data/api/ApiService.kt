package com.example.school_companion.data.api

import com.example.school_companion.data.model.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // Authentication
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<ResponseBody>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>
    
    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>
    
    // User Profile
    @GET("auth/me")
    suspend fun getUserProfile(@Header("Authorization") token: String): Response<Companion>
    
    @PUT("user/profile")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body companion: Companion
    ): Response<Companion>
    
    // Children
    @GET("child")
    suspend fun getChildren(@Header("Authorization") token: String): Response<List<Child>>
    
    @GET("child/{childId}")
    suspend fun getChild(
        @Header("Authorization") token: String,
        @Path("childId") childId: String
    ): Response<Child>
    
    // Events
    @GET("events")
    suspend fun getEvents(
        @Header("Authorization") token: String,
        @Query("childId") childId: String? = null,
        @Query("date") date: String? = null
    ): Response<List<Event>>
    
    @POST("events")
    suspend fun createEvent(
        @Header("Authorization") token: String,
        @Body event: Event
    ): Response<Event>
    
    @PUT("events/{eventId}")
    suspend fun updateEvent(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: String,
        @Body event: Event
    ): Response<Event>
    
    @DELETE("events/{eventId}")
    suspend fun deleteEvent(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: String
    ): Response<Unit>
    
    // Monitoring Data
    @GET("monitoring")
    suspend fun getMonitoringData(
        @Header("Authorization") token: String,
        @Query("childId") childId: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<List<MonitoringData>>
    
    @POST("monitoring")
    suspend fun createMonitoringData(
        @Header("Authorization") token: String,
        @Body monitoringData: MonitoringData
    ): Response<MonitoringData>
    
    @PUT("monitoring/{monitoringId}")
    suspend fun updateMonitoringData(
        @Header("Authorization") token: String,
        @Path("monitoringId") monitoringId: String,
        @Body monitoringData: MonitoringData
    ): Response<MonitoringData>
    
    // Notes
    @GET("notes")
    suspend fun getNotes(
        @Header("Authorization") token: String,
        @Query("childId") childId: String? = null
    ): Response<List<Note>>
    
    @POST("notes")
    suspend fun createNote(
        @Header("Authorization") token: String,
        @Body note: Note
    ): Response<Note>
    
    @PUT("notes/{noteId}")
    suspend fun updateNote(
        @Header("Authorization") token: String,
        @Path("noteId") noteId: String,
        @Body note: Note
    ): Response<Note>
    
    @DELETE("notes/{noteId}")
    suspend fun deleteNote(
        @Header("Authorization") token: String,
        @Path("noteId") noteId: String
    ): Response<Unit>
    
    // Work Sessions
    @GET("work-sessions")
    suspend fun getWorkSessions(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<List<WorkSession>>
    
    @POST("work-sessions/start")
    suspend fun startWorkSession(
        @Header("Authorization") token: String
    ): Response<WorkSession>
    
    @PUT("work-sessions/{sessionId}/end")
    suspend fun endWorkSession(
        @Header("Authorization") token: String,
        @Path("sessionId") sessionId: String
    ): Response<WorkSession>
    
    // Statistics
    @GET("statistics")
    suspend fun getStatistics(
        @Header("Authorization") token: String,
        @Query("childId") childId: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<StatisticsResponse>
}

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val role: String
)

data class AuthResponse(
    val token: String,
    val companion: Companion
)

data class StatisticsResponse(
    val emotionalStateData: Map<String, Int>,
    val behaviorPatternData: Map<String, Int>,
    val learningProgressData: Map<String, Int>,
    val socialInteractionData: Map<String, Int>,
    val totalWorkHours: Double,
    val totalChildren: Int,
    val totalEvents: Int
) 