package com.example.school_companion.data.api

import com.example.school_companion.data.model.Note
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface NoteApi {
    @POST("note/child/{childId}")
    suspend fun addNote(
        @Header("Authorization") token: String,
        @Body noteRequestDto: NoteRequestDto,
        @Path("childId") childId: Long
    ): Response<ResponseBody>

    @PATCH("note/{noteId}/child/{childId}")
    suspend fun updateNote(
        @Header("Authorization") token: String,
        @Body noteUpdateDto: NoteRequestDto,
        @Path("childId") childId: Long,
        @Path("noteId") noteId: Long
    ): Response<ResponseBody>

    @DELETE("note/{noteId}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("noteId") noteId: Long
    ): Response<ResponseBody>

    @GET("note/{noteId}")
    suspend fun getNoteById(
        @Header("Authorization") token: String,
        @Path("noteId") noteId: Long
    ): Response<Note>

    @GET("note")
    suspend fun getNotesByCompanion(
        @Header("Authorization") token: String,
    ): Response<List<Note>>

    @GET("note/child/{childId}")
    suspend fun getNotesByChild(
        @Header("Authorization") token: String,
        @Path("childId") childId: Long
    ): Response<List<Note>>
}

data class NoteRequestDto(
    val content: String
)


