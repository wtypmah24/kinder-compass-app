package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ChatApi
import com.example.school_companion.data.api.ChatRequest
import com.example.school_companion.data.model.AssistantAnswer
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val apiService: ChatApi
) {
    suspend fun askNewChat(
        prompt: ChatRequest,
        childId: Long
    ): Result<List<AssistantAnswer>> {
        return try {
            val response = apiService.askNewChat(prompt, childId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to ask AI Assistant: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun ask(
        childId: Long,
        prompt: ChatRequest,
        threadId: String
    ): Result<List<AssistantAnswer>> {
        return try {
            val response = apiService.ask(prompt, childId, threadId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to ask AI Assistant: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChatIds(): Result<List<String>> {
        return try {
            val response = apiService.getChatIds()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get chat ids: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChatByThreadId(threadId: String): Result<List<AssistantAnswer>> {
        return try {
            val response = apiService.getChatByThreadId(threadId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get chats by thread id: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeChatByThreadId(threadId: String): Result<String> {
        return try {
            val response = apiService.removeChatByThreadId(threadId)
            if (response.isSuccessful) {
                val message = response.body()?.string() ?: "Thread deleted successfully"
                Result.success(message)
            } else {
                Result.failure(Exception("Failed to delete Thread: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
