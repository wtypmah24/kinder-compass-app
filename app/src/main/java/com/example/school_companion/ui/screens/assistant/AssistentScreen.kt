package com.example.school_companion.ui.screens.assistant

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.school_companion.data.api.ChatRequest
import com.example.school_companion.data.model.AssistantAnswer
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.screens.monitoring.DropdownMenuWrapper
import com.example.school_companion.ui.viewmodel.AuthViewModel
import com.example.school_companion.ui.viewmodel.ChatViewModel
import com.example.school_companion.ui.viewmodel.ChildrenState
import com.example.school_companion.ui.viewmodel.ChildrenViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
    val chatState by chatViewModel.chatState.collectAsStateWithLifecycle()

    var selectedChild by remember { mutableStateOf<Child?>(null) }
    var selectedThread by remember { mutableStateOf<String?>(null) }
    var showHistory by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }

    LaunchedEffect(authToken) {
        authToken?.let { token ->
            childrenViewModel.loadChildren(token)
            chatViewModel.getChatIds(token)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showHistory = !showHistory }) {
                        Icon(Icons.Default.History, contentDescription = "History")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Выбор ребенка
            if (childrenState is ChildrenState.Success) {
                DropdownMenuWrapper(
                    items = (childrenState as ChildrenState.Success).children,
                    selectedItem = selectedChild,
                    onItemSelected = {
                        selectedChild = it
                        selectedThread = null
                        messageText = ""
                    },
                    itemToString = { "${it.name} ${it.surname}" },
                    placeholder = "Choose Child"
                )
            }

            // Выбор threadId (если есть)
            if (chatIdsState is ChatViewModel.ChatIdsState.Success && !showHistory) {
                DropdownMenuWrapper(
                    items = (chatIdsState as ChatViewModel.ChatIdsState.Success).children,
                    selectedItem = selectedThread,
                    onItemSelected = {
                        selectedThread = it
                        authToken?.let { token ->
                            chatViewModel.getChatByThreadId(token, it)
                        }
                    },
                    itemToString = { it },
                    placeholder = "Choose Chat Thread"
                )
            }

            // Режим истории (список threadId)
            if (showHistory) {
                when (chatIdsState) {
                    is ChatViewModel.ChatIdsState.Success -> {
                        LazyColumn {
                            items((chatIdsState as ChatViewModel.ChatIdsState.Success).children) { threadId ->
                                Text(
                                    text = threadId,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedThread = threadId
                                            authToken?.let { token ->
                                                chatViewModel.getChatByThreadId(token, threadId)
                                            }
                                            showHistory = false
                                        }
                                        .padding(8.dp)
                                )
                            }
                        }
                    }

                    is ChatViewModel.ChatIdsState.Error -> Text("Failed to load history")
                    else -> {}
                }
            } else {
                // Окно чата с рамкой
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                    tonalElevation = 2.dp,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        if (selectedThread == null) {
                            // Нет выбранного чата — просто пустое место
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No chat selected")
                            }
                        } else {
                            // Показываем чат
                            when (chatState) {
                                is ChatViewModel.ChatState.Success -> {
                                    val messages =
                                        (chatState as ChatViewModel.ChatState.Success).children
                                            .sortedBy { it.created_at }
                                    LazyColumn(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        items(messages) { msg ->
                                            ChatMessageItem(msg)
                                        }
                                    }
                                }

                                is ChatViewModel.ChatState.Error -> {
                                    Box(
                                        Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Failed to load chat")
                                    }
                                }

                                else -> {}
                            }
                        }

                        // Панель ввода сообщения
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (selectedChild == null) {
                                Text("To start chat you have to select a child")
                            } else {
                                OutlinedTextField(
                                    value = messageText,
                                    onValueChange = { messageText = it },
                                    modifier = Modifier.weight(1f),
                                    placeholder = { Text("Type your message") }
                                )
                                Button(
                                    onClick = {
                                        authToken?.let { token ->
                                            if (selectedThread == null) {
                                                // Новый чат
                                                chatViewModel.askNewChat(
                                                    token,
                                                    ChatRequest(messageText),
                                                    selectedChild!!.id
                                                ) { newThreadId ->
                                                    selectedThread = newThreadId
                                                    chatViewModel.getChatByThreadId(
                                                        token,
                                                        newThreadId
                                                    )
                                                }
                                            } else {
                                                // Продолжение чата
                                                chatViewModel.ask(
                                                    token,
                                                    selectedChild!!.id,
                                                    ChatRequest(messageText),
                                                    selectedThread!!
                                                )
                                            }
                                            messageText = ""
                                        }
                                    },
                                    enabled = messageText.isNotBlank(),
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Text("Send")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatMessageItem(msg: AssistantAnswer) {
    val time = Instant.ofEpochMilli(msg.created_at)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(DateTimeFormatter.ofPattern("HH:mm"))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (msg.role == "User") Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (msg.role == "User") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        ) {
            Column(Modifier.padding(8.dp)) {
                Text(
                    text = if (msg.role == "User") "You" else "AI Assistant",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(msg.message, color = Color.White)
                Text(time, fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
            }
        }
    }
}
