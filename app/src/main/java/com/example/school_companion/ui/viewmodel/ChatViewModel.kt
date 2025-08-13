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

    private val _chatState = MutableStateFlow<ChatState>(ChatState.Loading)
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    private val _chatIdsState = MutableStateFlow<ChatIdsState>(ChatIdsState.Loading)
    val chatIdsState: StateFlow<ChatIdsState> = _chatIdsState.asStateFlow()

    private val _selectedChat = MutableStateFlow<AssistantAnswer?>(null)
    val selectedChat: StateFlow<AssistantAnswer?> = _selectedChat.asStateFlow()

    fun askNewChat(
        token: String,
        prompt: ChatRequest,
        childId: Long,
    ) {
        viewModelScope.launch {
            _chatState.value = ChatState.Loading
            chatRepository.askNewChat(token, prompt, childId).collect { result ->
                result.fold(
                    onSuccess = { answers ->
                        _chatState.value = ChatState.Success(answers)
                    },
                    onFailure = { exception ->
                        _chatState.value =
                            ChatState.Error(exception.message ?: "Failed to send question")
                    }
                )
            }
        }
    }

    fun ask(
        token: String,
        childId: Long,
        prompt: ChatRequest,
        threadId: String
    ) {
        viewModelScope.launch {
            _chatState.value = ChatState.Loading
            chatRepository.ask(token, childId, prompt, threadId).collect { result ->
                result.fold(
                    onSuccess = { answers ->
                        _chatState.value = ChatState.Success(answers)
                    },
                    onFailure = { exception ->
                        _chatState.value =
                            ChatState.Error(exception.message ?: "Failed to send question")
                    }
                )
            }
        }
    }

    fun getChatIds(token: String) {
        viewModelScope.launch {
            _chatIdsState.value = ChatIdsState.Loading
            chatRepository.getChatIds(token).collect { result ->
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
    }


    fun getChatByThreadId(token: String, threadId: String) {
        viewModelScope.launch {
            _chatState.value = ChatState.Loading
            chatRepository.getChatByThreadId(token, threadId).collect { result ->
                result.fold(
                    onSuccess = { answer ->
                        _chatState.value = ChatState.Success(answer)
                    },
                    onFailure = { exception ->
                        _chatState.value =
                            ChatState.Error(exception.message ?: "Failed to get thread ids")
                    }
                )
            }
        }
    }

    fun removeChatByThreadId(token: String, threadId: String) {
        viewModelScope.launch {
            _chatState.value = ChatState.Loading
            chatRepository.removeChatByThreadId(token, threadId).collect { result ->
                result.fold(
                    onSuccess = {
                    },
                    onFailure = { exception ->
                        _chatState.value =
                            ChatState.Error(exception.message ?: "Failed to remove thread")
                    }
                )
            }
        }
    }

    sealed class ChatState {
        data object Loading : ChatState()
        data class Success(val answers: List<AssistantAnswer>) : ChatState()
        data class Error(val message: String) : ChatState()
    }

    sealed class ChatIdsState {
        data object Loading : ChatIdsState()
        data class Success(val children: List<String>) : ChatIdsState()
        data class Error(val message: String) : ChatIdsState()
    }
}