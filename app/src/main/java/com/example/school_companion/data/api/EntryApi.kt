package com.example.school_companion.data.api

import com.example.school_companion.data.model.MonitoringEntry
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface EntryApi {
    @POST("entry/child/{childId}/param/{paramId}")
    suspend fun addEntry(
        @Header("Authorization") token: String,
        @Body entryRequestDto: EntryRequestDto,
        @Path("childId") childId: Long,
        @Path("paramId") paramId: Long
    ): Response<ResponseBody>

    @PATCH("entry/{entryId}/child/{childId}")
    suspend fun updateEntry(
        @Header("Authorization") token: String,
        @Body entryUpdateDto: EntryRequestDto,
        @Path("childId") childId: Long,
        @Path("entryId") entryId: Long
    ): Response<ResponseBody>

    @DELETE("entry/{entryId}/child/{childId}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("entryId") entryId: Long,
        @Path("childId") childId: Long
    ): Response<ResponseBody>

    @GET("entry/{entryId}")
    suspend fun getEntryById(
        @Header("Authorization") token: String,
        @Path("entryId") noteId: Long
    ): Response<MonitoringEntry>

    @GET("entry")
    suspend fun getEntriesByCompanion(
        @Header("Authorization") token: String,
    ): Response<List<MonitoringEntry>>

    @GET("entry/child/{childId}")
    suspend fun getEntriesByChild(
        @Header("Authorization") token: String,
        @Path("childId") childId: Long
    ): Response<List<MonitoringEntry>>
}

data class EntryRequestDto(
    val value: String,
    val notes: String
)


