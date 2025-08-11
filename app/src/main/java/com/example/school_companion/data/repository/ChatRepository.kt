package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ChatApi
import com.example.school_companion.data.api.ChatRequest
import com.example.school_companion.data.model.AssistantAnswer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val apiService: ChatApi
) {
    suspend fun askNewChat(
        token: String,
        prompt: ChatRequest,
        childId: Long
    ): Flow<Result<List<AssistantAnswer>>> = flow {
        try {
            val response = apiService.askNewChat("Bearer $token", prompt, childId)
            if (response.isSuccessful) {
                response.body()?.let { children ->
                    emit(Result.success(children))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to ask AI Assistant: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun ask(
        token: String,
        childId: Long,
        prompt: ChatRequest,
        threadId: String
    ): Flow<Result<List<AssistantAnswer>>> = flow {
        val response = apiService.ask("Bearer $token", prompt, childId, threadId)
        if (response.isSuccessful) {
            response.body()?.let { child ->
                emit(Result.success(child))
            } ?: emit(Result.failure(Exception("Empty response")))
        } else {
            emit(Result.failure(Exception("Failed to ask AI Assistant: ${response.code()}")))
        }
    }.catch { e ->
        emit(Result.failure(e))
    }

    suspend fun getChatIds(token: String): Flow<Result<List<String>>> = flow {
        try {
            val response = apiService.getChatIds("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { photos ->
                    emit(Result.success(photos))
                } ?: emit(Result.failure(Exception("Empty response")))
            } else {
                emit(Result.failure(Exception("Failed to get chat ids: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun getChatByThreadId(
        token: String,
        threadId: String
    ): Flow<Result<List<AssistantAnswer>>> = flow {

        val response = apiService.getChatByThreadId("Bearer $token", threadId)
        if (response.isSuccessful) {
            response.body()?.let { photos ->
                emit(Result.success(photos))
            } ?: emit(Result.failure(Exception("Empty response")))
        } else {
            emit(Result.failure(Exception("Failed to get chats by thread id: ${response.code()}")))
        }
    }

    suspend fun removeChatByThreadId(token: String, threadId: String) =
        flow {
            try {
                val response = apiService.removeChatByThreadId("Bearer $token", threadId)
                if (response.isSuccessful) {
                    val message = response.body()?.string() ?: "Thread deleted successfully"
                    emit(Result.success(message))
                } else {
                    emit(Result.failure(Exception("Failed to delete Thread: ${response.code()}")))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
} 