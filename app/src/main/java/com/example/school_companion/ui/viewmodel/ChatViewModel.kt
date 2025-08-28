package com.example.school_companion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school_companion.data.api.ChatRequest
import com.example.school_companion.data.model.AssistantAnswer
import com.example.school_companion.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<AssistantAnswer>>(emptyList())
    val messages: StateFlow<List<AssistantAnswer>> = _messages.asStateFlow()

    private val _chatIdsState = MutableStateFlow<ChatIdsState>(ChatIdsState.Loading)
    val chatIdsState: StateFlow<ChatIdsState> = _chatIdsState.asStateFlow()

    private val _selectedThreadId = MutableStateFlow<String?>(null)
    val selectedThreadId: StateFlow<String?> = _selectedThreadId.asStateFlow()

    private fun askNewChat(prompt: ChatRequest, childId: Long) {
        viewModelScope.launch {
            val result = chatRepository.askNewChat(prompt, childId)
            result.fold(
                onSuccess = { answers ->
                    _messages.value = answers
                    if (answers.isNotEmpty()) {
                        _selectedThreadId.value = answers.first().thread_id
                        getChatIds()
                    }
                },
                onFailure = { exception ->
                }
            )
        }
    }

    private fun ask(childId: Long, prompt: ChatRequest, threadId: String) {
        viewModelScope.launch {
            val result = chatRepository.ask(childId, prompt, threadId)
            result.fold(
                onSuccess = { answers ->
                    _messages.value = answers
                },
                onFailure = { exception ->
                }
            )
        }
    }

    fun getChatIds() {
        viewModelScope.launch {
            val result = chatRepository.getChatIds()
            result.fold(
                onSuccess = { ids ->
                    _chatIdsState.value = ChatIdsState.Success(ids)
                },
                onFailure = { exception ->
                    _chatIdsState.value =
                        ChatIdsState.Error(exception.message ?: "Failed to get chat ids")
                }
            )
        }
    }

    fun getChatByThreadId(threadId: String) {
        viewModelScope.launch {
            val result = chatRepository.getChatByThreadId(threadId)
            result.fold(
                onSuccess = { answers ->
                    _messages.value = answers
                },
                onFailure = { exception ->
                }
            )
        }
    }

    fun removeChatByThreadId(threadId: String) {
        viewModelScope.launch {
            val result = chatRepository.removeChatByThreadId(threadId)
            result.fold(
                onSuccess = {
                    if (_selectedThreadId.value == threadId) {
                        _messages.value = emptyList()
                        _selectedThreadId.value = null
                    }
                    getChatIds()
                },
                onFailure = { exception ->
                }
            )
        }
    }

    private fun addTemporaryMessages(userMsg: AssistantAnswer, typingMsg: AssistantAnswer) {
        _messages.value = _messages.value + userMsg + typingMsg
    }

    fun clearMessages() {
        _messages.value = emptyList()
    }

    fun sendMessage(
        messageText: String,
        selectedChildId: Long?,
        selectedThread: String?
    ) {
        val now = System.currentTimeMillis()

        val userMsg = AssistantAnswer(
            id = "TEMP_USER_$now",
            role = "user",
            message = messageText,
            thread_id = selectedThread ?: "",
            created_at = now
        )

        val typingMsg = AssistantAnswer(
            id = "TEMP_ASSISTANT_ID",
            role = "assistant",
            message = "...",
            thread_id = selectedThread ?: "",
            created_at = now + 1
        )

        addTemporaryMessages(userMsg, typingMsg)

        if (selectedThread == null) {
            selectedChildId?.let { childId ->
                askNewChat(ChatRequest(messageText), childId)
            }
        } else {
            ask(selectedChildId ?: 0L, ChatRequest(messageText), selectedThread)
        }
    }

    sealed class ChatIdsState {
        data object Loading : ChatIdsState()
        data class Success(val children: List<String>) : ChatIdsState()
        data class Error(val message: String) : ChatIdsState()
    }
}
