package com.example.school_companion.data.repository

import com.example.school_companion.data.api.ChatApi
import com.example.school_companion.data.api.ChatRequest
import com.example.school_companion.data.model.AssistantAnswer
import com.example.school_companion.ui.util.toResult
import com.example.school_companion.ui.util.toResultUnit
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val apiService: ChatApi
) {
    suspend fun askNewChat(prompt: ChatRequest, childId: Long): Result<List<AssistantAnswer>> {
        return try {
            apiService.askNewChat(prompt, childId).toResult()
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
            apiService.ask(prompt, childId, threadId).toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChatIds(): Result<List<String>> {
        return try {
            apiService.getChatIds().toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChatByThreadId(threadId: String): Result<List<AssistantAnswer>> {
        return try {
            apiService.getChatByThreadId(threadId).toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeChatByThreadId(threadId: String): Result<Unit> {
        return try {
            apiService.removeChatByThreadId(threadId).toResultUnit()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
