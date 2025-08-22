package com.example.school_companion.ui.screens.assistant

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.school_companion.data.model.AssistantAnswer
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.card.chat.MessageInputCard
import com.example.school_companion.ui.list.MessagesList
import com.example.school_companion.ui.selector.ChildSelector
import com.example.school_companion.ui.selector.ThreadSelector
import com.example.school_companion.ui.viewmodel.AuthViewModel
import com.example.school_companion.ui.viewmodel.ChatViewModel
import com.example.school_companion.ui.viewmodel.ChildrenState
import com.example.school_companion.ui.viewmodel.ChildrenViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    childrenViewModel: ChildrenViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel(),
) {
    val authToken by authViewModel.authToken.collectAsStateWithLifecycle()
    val childrenState by childrenViewModel.childrenState.collectAsStateWithLifecycle()
    val chatIdsState by chatViewModel.chatIdsState.collectAsStateWithLifecycle()

    var selectedChild by remember { mutableStateOf<Child?>(null) }
    var selectedThread by remember { mutableStateOf<String?>(null) }
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current

    val messages by chatViewModel.messages.collectAsStateWithLifecycle()

    LaunchedEffect(authToken) {
        authToken?.let { token ->
            childrenViewModel.loadChildren(token)
            chatViewModel.getChatIds(token)
        }
    }

    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            coroutine.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                "AI Assistant", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }, actions = {
            IconButton(onClick = {
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
            }) {
                Icon(Icons.Filled.Message, contentDescription = "New chat")
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (childrenState is ChildrenState.Success) {
                ChildSelector(children = (childrenState as ChildrenState.Success).children,
                    selectedChild = selectedChild,
                    onSelect = {
                        selectedChild = it
                        selectedThread = null
                        chatViewModel.clearMessages()
                        messageText = ""
                    })
            }

            if (chatIdsState is ChatViewModel.ChatIdsState.Success) {
                ThreadSelector(threads = (chatIdsState as ChatViewModel.ChatIdsState.Success).children,
                    selectedThread = selectedThread,
                    onSelect = { tid ->
                        selectedThread = tid
                        selectedChild = null
                        authToken?.let { chatViewModel.getChatByThreadId(it, tid) }
                    },
                    onDelete = { tid ->
                        authToken?.let {
                            chatViewModel.removeChatByThreadId(it, tid)
                            chatViewModel.getChatIds(it)
                        }
                        if (tid == selectedThread) chatViewModel.clearMessages()
                        selectedThread = null
                    })
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium),
                tonalElevation = 2.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                Column {
                    MessagesList(
                        messages = messages, listState = listState, modifier = Modifier.weight(1f)
                    )
                }
            }
            MessageInputCard(messageText = messageText,
                onMessageChange = { messageText = it },
                canSend = (selectedChild != null) || (selectedThread != null),
                onSend = {
                    authToken?.let { token ->
                        chatViewModel.sendMessage(
                            token = token,
                            messageText = messageText,
                            selectedChildId = selectedChild?.id,
                            selectedThread = selectedThread
                        )
                        messageText = ""
                    }
                }
            )
        }
    }
}