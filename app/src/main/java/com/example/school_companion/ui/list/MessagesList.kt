package com.example.school_companion.ui.list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.model.AssistantAnswer
import com.example.school_companion.ui.card.chat.ChatMessageCard

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