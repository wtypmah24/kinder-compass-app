package com.example.school_companion.ui.tab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.api.EventRequestDto
import com.example.school_companion.data.model.Event
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.card.child.ChildEventCard
import com.example.school_companion.ui.dialog.event.AddEventDialog
import com.example.school_companion.ui.viewmodel.UiState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventsTab(
    eventsState: UiState<List<Event>>,
    onAddEvent: (EventRequestDto) -> Unit,
    onEditEvent: (Event, EventRequestDto) -> Unit,
    onDeleteEvent: (Event) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) { Text("Add Event") }

        when (eventsState) {
            is UiState.Loading -> LoadingBox()
            is UiState.Error -> ErrorBox(eventsState.message)
            is UiState.Success -> {
                val events = eventsState.data
                if (events.isEmpty()) {
                    Text("Keine Events für Child")
                } else {
                    LazyColumn {
                        items(events) { event ->
                            ChildEventCard(
                                event = event,
                                onEdit = { dto -> onEditEvent(event, dto) },
                                onDelete = { onDeleteEvent(event) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddEventDialog(
            onDismiss = { showAddDialog = false },
            onSave = { eventDto ->
                onAddEvent(eventDto)
                showAddDialog = false
            }
        )
    }
}
