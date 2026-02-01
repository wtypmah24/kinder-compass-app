package com.example.school_companion.feature.assistant

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.model.AssistantAnswer
import com.example.school_companion.data.model.Child
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.selector.GenericSelector
import com.example.school_companion.ui.util.UiState
import com.example.school_companion.ui.util.onState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantContent(
    onNavigate: NavigateToWithArgs,
    childrenState: UiState<List<Child>>,
    chatIdsState: UiState<List<String>>,
    messagesState: UiState<List<AssistantAnswer>>,
    selectedChild: Child?,
    onSelectChild: (Child?) -> Unit,
    selectedThread: String?,
    onSelectThread: (String?) -> Unit,
    onDeleteThread: (String) -> Unit,
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onNewChat: () -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("AI Assistant", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { onNavigate(null, null) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = onNewChat) {
                    Icon(Icons.AutoMirrored.Filled.Message, contentDescription = "New chat")
                }
            }
        )
    }) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (childrenState is UiState.Success) {
                GenericSelector(
                    items = childrenState.data,
                    selectedItem = selectedChild,
                    onSelect = onSelectChild,
                    itemToString = { "${it.name} ${it.surname}" },
                    placeholder = "Choose Child"
                )
            }

            if (chatIdsState is UiState.Success) {
                ThreadSelector(
                    threads = chatIdsState.data,
                    selectedThread = selectedThread,
                    onSelect = onSelectThread,
                    onDelete = onDeleteThread
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
                    messagesState.onState(
                        onLoading = { LoadingBox() },
                        onError = { msg -> ErrorBox(message = msg) },
                        onSuccess = {
                            MessagesList(
                                messages = it,
                                listState = listState,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    )
                }
            }

            MessageInputCard(
                messageText = messageText,
                onMessageChange = onMessageChange,
                canSend = (selectedChild != null) || (selectedThread != null),
                onSend = onSendMessage
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Empty State")
@Composable
fun AssistantContentPreview_Empty() {
    AssistantContent(
        onNavigate = { _, _ -> },
        childrenState = UiState.Success(
            listOf(
                previewChild
            )
        ),
        chatIdsState = UiState.Success(emptyList()),
        messagesState = UiState.Success(emptyList()),
        selectedChild = null,
        onSelectChild = {},
        selectedThread = null,
        onSelectThread = {},
        onDeleteThread = {},
        messageText = "",
        onMessageChange = {},
        onSendMessage = {},
        onNewChat = {},
        listState = rememberLazyListState()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "With Messages")
@Composable
fun AssistantContentPreview_WithMessages() {
    AssistantContent(
        onNavigate = { _, _ -> },
        childrenState = UiState.Success(
            listOf(
                previewChild
            )
        ),
        chatIdsState = UiState.Success(listOf("thread-1")),
        messagesState = UiState.Success(
            previewAssistantAnswers
        ),
        selectedChild = previewChild,
        onSelectChild = {},
        selectedThread = "thread-1",
        onSelectThread = {},
        onDeleteThread = {},
        messageText = "New question...",
        onMessageChange = {},
        onSendMessage = {},
        onNewChat = {},
        listState = rememberLazyListState()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Loading")
@Composable
fun AssistantContentPreview_Loading() {
    AssistantContent(
        onNavigate = { _, _ -> },
        childrenState = UiState.Loading,
        chatIdsState = UiState.Loading,
        messagesState = UiState.Loading,
        selectedChild = null,
        onSelectChild = {},
        selectedThread = null,
        onSelectThread = {},
        onDeleteThread = {},
        messageText = "",
        onMessageChange = {},
        onSendMessage = {},
        onNewChat = {},
        listState = rememberLazyListState()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Error")
@Composable
fun AssistantContentPreview_Error() {
    AssistantContent(
        onNavigate = { _, _ -> },
        childrenState = UiState.Error("Error during loading children"),
        chatIdsState = UiState.Error("Error during loading chats"),
        messagesState = UiState.Error("Error during loading messages"),
        selectedChild = null,
        onSelectChild = {},
        selectedThread = null,
        onSelectThread = {},
        onDeleteThread = {},
        messageText = "",
        onMessageChange = {},
        onSendMessage = {},
        onNewChat = {},
        listState = rememberLazyListState()
    )
}

val previewChild = Child(
    id = 1L,
    name = "Alice",
    surname = "Johnson",
    email = "alice.johnson@example.com",
    phoneNumber = "+1234567890",
    dateOfBirth = "2015-06-21",
    active = true
)

val previewAssistantAnswers = listOf(
    AssistantAnswer(
        id = "msg-1",
        thread_id = "thread-1",
        role = "user",
        message = "Hello! I need help with a kind.",
        created_at = System.currentTimeMillis() - 60_000
    ),
    AssistantAnswer(
        id = "msg-2",
        thread_id = "thread-1",
        role = "assistant",
        message = "Sure! Tell me more about the kid!",
        created_at = System.currentTimeMillis()
    )
)

