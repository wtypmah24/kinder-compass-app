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

    private val _messages = MutableStateFlow<UiState<List<AssistantAnswer>>>(UiState.Idle)
    val messages: StateFlow<UiState<List<AssistantAnswer>>> = _messages.asStateFlow()

    private val _chatIdsState = MutableStateFlow<UiState<List<String>>>(UiState.Loading)
    val chatIdsState: StateFlow<UiState<List<String>>> = _chatIdsState.asStateFlow()

    private val _selectedThreadId = MutableStateFlow<String?>(null)
    val selectedThreadId: StateFlow<String?> = _selectedThreadId.asStateFlow()

    private fun askNewChat(prompt: ChatRequest, childId: Long) {
        viewModelScope.launch {
            _messages.value = UiState.Loading
            val result = chatRepository.askNewChat(prompt, childId)
            handleResult(result, _messages) { answers ->
                if (answers.isNotEmpty()) {
                    _selectedThreadId.value = answers.first().thread_id
                    getChatIds()
                }
            }
        }
    }

    private fun ask(childId: Long, prompt: ChatRequest, threadId: String) {
        viewModelScope.launch {
            _messages.value = UiState.Loading
            val result = chatRepository.ask(childId, prompt, threadId)
            handleResult(result, _messages)
        }
    }

    fun getChatIds() {
        viewModelScope.launch {
            _chatIdsState.value = UiState.Loading
            val result = chatRepository.getChatIds()
            handleResult(result, _chatIdsState)
        }
    }

    fun getChatByThreadId(threadId: String) {
        viewModelScope.launch {
            _messages.value = UiState.Loading
            val result = chatRepository.getChatByThreadId(threadId)
            handleResult(result, _messages)
        }
    }

    fun removeChatByThreadId(threadId: String) {
        viewModelScope.launch {
            val result = chatRepository.removeChatByThreadId(threadId)
            handleResult(result, MutableStateFlow(UiState.Idle)) {
                if (_selectedThreadId.value == threadId) {
                    _messages.value = UiState.Success(emptyList())
                    _selectedThreadId.value = null
                }
                getChatIds()
            }
        }
    }

    private fun addTemporaryMessages(userMsg: AssistantAnswer, typingMsg: AssistantAnswer) {
        val current = (_messages.value as? UiState.Success)?.data ?: emptyList()
        _messages.value = UiState.Success(current + userMsg + typingMsg)
    }

    fun clearMessages() {
        _messages.value = UiState.Success(emptyList())
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
}
