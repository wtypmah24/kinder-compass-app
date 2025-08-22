package com.example.school_companion.ui.card.chat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MessageInputCard(
    messageText: String, onMessageChange: (String) -> Unit, canSend: Boolean, onSend: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
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
            onClick = onSend, enabled = canSend && messageText.isNotBlank()
        ) {
            Text("Send")
        }
    }
}