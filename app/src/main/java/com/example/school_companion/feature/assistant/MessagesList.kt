package com.example.school_companion.feature.assistant

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.model.AssistantAnswer

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessagesList(
    messages: List<AssistantAnswer>, listState: LazyListState, modifier: Modifier = Modifier
) {
    if (messages.isNotEmpty()) {
        val sortedMessages = messages.sortedBy { it.created_at }
        LazyColumn(
            state = listState,
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(sortedMessages) { msg ->
                ChatMessageCard(msg)
            }
        }
    } else {
        Box(
            modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            Text("No messages")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Messages List - With Messages")
@Composable
fun MessagesListPreview_WithMessages() {
    val previewMessages = listOf(
        AssistantAnswer(
            id = "msg-1",
            thread_id = "thread-1",
            role = "user",
            message = "Hi! How are you?",
            created_at = System.currentTimeMillis() - 60_000
        ),
        AssistantAnswer(
            id = "msg-2",
            thread_id = "thread-1",
            role = "assistant",
            message = "Hello! I’m doing well 😊 How can I help you today?",
            created_at = System.currentTimeMillis()
        ),
        AssistantAnswer(
            id = "TEMP_ASSISTANT_ID",
            thread_id = "thread-1",
            role = "assistant",
            message = "",
            created_at = System.currentTimeMillis()
        )
    )

    MessagesList(
        messages = previewMessages,
        listState = rememberLazyListState()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Messages List - Empty")
@Composable
fun MessagesListPreview_Empty() {
    MessagesList(
        messages = emptyList(),
        listState = rememberLazyListState()
    )
}
