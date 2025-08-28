package com.example.school_companion.data.api

import com.example.school_companion.data.model.AssistantAnswer
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {
    @POST("chat/child/{childId}")
    suspend fun askNewChat(
        @Body chatRequest: ChatRequest,
        @Path("childId") childId: Long
    ): Response<List<AssistantAnswer>>

    @POST("chat/child/{childId}/thread/{threadId}")
    suspend fun ask(
        @Body chatRequest: ChatRequest,
        @Path("childId") childId: Long,
        @Path("threadId") threadId: String
    ): Response<List<AssistantAnswer>>

    @GET("chat/threads")
    suspend fun getChatIds(): Response<List<String>>

    @GET("chat/thread/{threadId}")
    suspend fun getChatByThreadId(
        @Path("threadId") threadId: String
    ): Response<List<AssistantAnswer>>

    @DELETE("chat/thread/{threadId}")
    suspend fun removeChatByThreadId(
        @Path("threadId") threadId: String
    ): Response<ResponseBody>
}

data class ChatRequest(
    val message: String,
)

