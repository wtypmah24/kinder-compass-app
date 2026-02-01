package com.example.school_companion.feature.assistant

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.data.model.Child
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.ui.util.UiState
import com.example.school_companion.ui.util.getOrNull
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantScreen(
    onNavigate: NavigateToWithArgs,
    childrenState: UiState<List<Child>>,
    chatViewModel: ChatViewModel = hiltViewModel(),
) {
    val chatIdsState by chatViewModel.chatIdsState.collectAsStateWithLifecycle()
    val messages by chatViewModel.messages.collectAsStateWithLifecycle()

    var selectedChild by remember { mutableStateOf<Child?>(null) }
    var selectedThread by remember { mutableStateOf<String?>(null) }
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        chatViewModel.getChatIds()
    }

    LaunchedEffect(messages) {
        messages.getOrNull()?.let { list ->
            if (list.isNotEmpty()) {
                coroutine.launch {
                    listState.animateScrollToItem(list.size - 1)
                }
            }
        }
    }

    AssistantContent(
        onNavigate = onNavigate,
        childrenState = childrenState,
        chatIdsState = chatIdsState,
        messagesState = messages,
        selectedChild = selectedChild,
        onSelectChild = {
            selectedChild = it
            selectedThread = null
            chatViewModel.clearMessages()
            messageText = ""
        },
        selectedThread = selectedThread,
        onSelectThread = { tid ->
            selectedThread = tid
            selectedChild = null
            if (tid != null) chatViewModel.getChatByThreadId(tid)
        },
        onDeleteThread = { tid ->
            chatViewModel.removeChatByThreadId(tid)
            chatViewModel.getChatIds()
            if (tid == selectedThread) chatViewModel.clearMessages()
            selectedThread = null
        },
        messageText = messageText,
        onMessageChange = { messageText = it },
        onSendMessage = {
            chatViewModel.sendMessage(
                messageText = messageText,
                selectedChildId = selectedChild?.id,
                selectedThread = selectedThread
            )
            messageText = ""
        },
        onNewChat = {
            if (selectedChild != null) {
                selectedThread = null
                chatViewModel.clearMessages()
                messageText = ""
            } else {
                Toast.makeText(
                    context,
                    "To start a new chat you need to choose a child",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        listState = listState
    )
}