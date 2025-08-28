package com.example.school_companion.data.api

import com.example.school_companion.data.model.Task
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import java.time.Instant

interface TaskApi {
    @POST("task/child/{childId}")
    suspend fun addTask(
        @Body taskRequestDto: TaskRequestDto,
        @Path("childId") childId: Long
    ): Response<ResponseBody>

    @PATCH("task/{taskId}/child/{childId}")
    suspend fun updateTask(
        @Body taskRequestDto: TaskRequestDto,
        @Path("childId") childId: Long,
        @Path("taskId") noteId: Long
    ): Response<ResponseBody>

    @DELETE("task/{taskId}/child/{childId}")
    suspend fun delete(
        @Path("taskId") noteId: Long,
        @Path("childId") childId: Long
    ): Response<ResponseBody>

    @GET("task/{taskId}")
    suspend fun getTaskById(
        @Path("taskId") noteId: Long
    ): Response<Task>

    @GET("task")
    suspend fun getNotesByCompanion(
    ): Response<List<Task>>

    @GET("task/child/{childId}")
    suspend fun getTasksByChild(
        @Path("childId") childId: Long
    ): Response<List<Task>>
}

data class TaskRequestDto(
    val title: String,
    val description: String,
    val status: String,
    val deadLine: Instant
)
