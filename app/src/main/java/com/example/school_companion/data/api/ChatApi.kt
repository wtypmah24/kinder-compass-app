package com.example.school_companion.data.api

import com.example.school_companion.data.model.AssistantAnswer
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.Photo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.time.LocalDate

interface ChatApi {
    @POST("chat/child/{childId}")
    suspend fun askNewChat(
        @Header("Authorization") token: String,
        @Body chatRequest: ChatRequest,
        @Path("childId") childId: Long
    ): Response<List<AssistantAnswer>>

    @POST("chat/child/{childId}/thread/{threadId}")
    suspend fun ask(
        @Header("Authorization") token: String,
        @Body chatRequest: ChatRequest,
        @Path("childId") childId: Long,
        @Path("threadId") threadId: String
    ): Response<List<AssistantAnswer>>

    @GET("chat/threads")
    suspend fun getChatIds(
        @Header("Authorization") token: String,
    ): Response<List<String>>

    @GET("chat/thread/{threadId}")
    suspend fun getChatByThreadId(
        @Header("Authorization") token: String,
        @Path("threadId") threadId: String
    ): Response<List<AssistantAnswer>>

    @DELETE("chat/thread/{threadId}")
    suspend fun removeChatByThreadId(
        @Header("Authorization") token: String,
        @Path("threadId") threadId: String
    ): Response<ResponseBody>
}

data class ChatRequest(
    val message: String,
)

