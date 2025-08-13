package com.example.school_companion.ui.screens.assistant

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.launch
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
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current

    var localMessages by remember { mutableStateOf<List<AssistantAnswer>>(emptyList()) }

    LaunchedEffect(authToken) {
        authToken?.let { token ->
            childrenViewModel.loadChildren(token)
            chatViewModel.getChatIds(token)
        }
    }

    LaunchedEffect(chatState) {
        if (chatState is ChatViewModel.ChatState.Success) {
            val answers =
                (chatState as ChatViewModel.ChatState.Success).answers.sortedBy { it.created_at }
            if (answers.isEmpty()) return@LaunchedEffect

            val answerThread = answers.first().thread_id

            if (selectedThread == null) {
                selectedThread = answerThread
                localMessages = answers
                authToken?.let { token -> chatViewModel.getChatIds(token) }
            } else if (selectedThread == answerThread) {
                localMessages = answers
            } else {
                val lastAssistant = answers.lastOrNull { it.role == "assistant" }
                if (lastAssistant != null) {
                    localMessages = localMessages.map {
                        if (it.id == "TEMP_ASSISTANT_ID") lastAssistant else it
                    }
                }
            }
            if (localMessages.isNotEmpty()) {
                coroutine.launch {
                    listState.animateScrollToItem(localMessages.size - 1)
                }
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
                    localMessages = emptyList()
                    messageText = ""
                } else {
                    Toast
                        .makeText(
                            context,
                            "To start a new chat you need to choose a child",
                            Toast.LENGTH_SHORT
                        )
                        .show()
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
                ChildSelector(
                    children = (childrenState as ChildrenState.Success).children,
                    selectedChild = selectedChild,
                    onSelect = {
                        selectedChild = it
                        messageText = ""
                    }
                )
            }

            if (chatIdsState is ChatViewModel.ChatIdsState.Success) {
                ThreadSelector(
                    threads = (chatIdsState as ChatViewModel.ChatIdsState.Success).children,
                    selectedThread = selectedThread,
                    onSelect = { tid ->
                        selectedThread = tid
                        authToken?.let { chatViewModel.getChatByThreadId(it, tid) }
                    },
                    onDelete = { tid ->
                        authToken?.let {
                            chatViewModel.removeChatByThreadId(it, tid)
                            chatViewModel.getChatIds(it)
                        }
                        if (tid == selectedThread) localMessages = emptyList()
                        selectedThread = null
                    }
                )
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
                        messages = localMessages,
                        listState = listState,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            MessageInput(
                messageText = messageText,
                onMessageChange = { messageText = it },
                canSend = (selectedChild != null) || (selectedThread != null),
                onSend = {
                    authToken?.let { token ->
                        val now = System.currentTimeMillis()

                        val userMsg = AssistantAnswer(
                            id = "TEMP_USER_$now",
                            role = "User",
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

                        localMessages = localMessages + userMsg + typingMsg

                        if (selectedThread == null) {
                            selectedChild?.let { child ->
                                chatViewModel.askNewChat(
                                    token,
                                    ChatRequest(messageText),
                                    child.id
                                )
                            }
                        } else {
                            chatViewModel.ask(
                                token,
                                selectedChild?.id ?: 0L,
                                ChatRequest(messageText),
                                selectedThread!!
                            )
                        }
                        messageText = ""
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatMessageItem(msg: AssistantAnswer) {
    val isUser = msg.role.equals("User", ignoreCase = true)
    val time = Instant.ofEpochMilli(msg.created_at)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(DateTimeFormatter.ofPattern("HH:mm"))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.Start else Arrangement.End
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = if (isUser) "You" else "AI Assistant",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (msg.id == "TEMP_ASSISTANT_ID") {
                    AnimatedDots()
                } else {
                    Text(
                        text = msg.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = time,
                    style = MaterialTheme.typography.labelSmall,
                    color = (if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary).copy(
                        alpha = 0.8f
                    )
                )
            }
        }
    }
}

@Composable
fun ChildSelector(
    children: List<Child>,
    selectedChild: Child?,
    onSelect: (Child) -> Unit
) {
    DropdownMenuWrapper(
        items = children,
        selectedItem = selectedChild,
        onItemSelected = onSelect,
        itemToString = { "${it.name} ${it.surname}" },
        placeholder = "Choose Child"
    )
}

@Composable
fun ThreadSelector(
    threads: List<String>,
    selectedThread: String?,
    onSelect: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    var threadToDelete by remember { mutableStateOf<String?>(null) }

    DropdownMenuWrapper(
        items = threads,
        selectedItem = selectedThread,
        onItemSelected = onSelect,
        itemToString = { it },
        placeholder = "Choose Chat Thread",
        customItemContent = { tid ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(tid, modifier = Modifier.weight(1f))
                IconButton(onClick = { threadToDelete = tid }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete chat",
                        tint = Color.Red
                    )
                }
            }
        }
    )

    if (threadToDelete != null) {
        AlertDialog(
            onDismissRequest = { threadToDelete = null },
            title = { Text("Delete chat with AI?") },
            text = { Text("Are you sure you want to delete ${threadToDelete}?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(threadToDelete!!)
                        threadToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { threadToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessagesList(
    messages: List<AssistantAnswer>,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    if (messages.isNotEmpty()) {
        LazyColumn(
            state = listState,
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(messages) { msg ->
                ChatMessageItem(msg)
            }
        }
    } else {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text("No messages")
        }
    }
}

@Composable
fun MessageInput(
    messageText: String,
    onMessageChange: (String) -> Unit,
    canSend: Boolean,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = onMessageChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                if (!canSend) Text("To start chat you have to select a child")
                else Text("Type your message")
            },
            enabled = canSend,
            singleLine = false,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onSend,
            enabled = canSend && messageText.isNotBlank()
        ) {
            Text("Send")
        }
    }
}

@Composable
fun AnimatedDots() {
    var dotCount by remember { mutableIntStateOf(1) }
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(500)
            dotCount = (dotCount % 3) + 1
        }
    }
    Text(".".repeat(dotCount))

}