package com.example.school_companion.feature.event

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.api.EventRequestDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.Event
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventsTabContent(
    child: Child,
    eventsState: EventsState,
    showAddDialog: Boolean,
    onAddClick: () -> Unit,
    onDismissAddDialog: () -> Unit,
    onSaveEvent: (EventRequestDto) -> Unit,
    onEditEvent: (eventId: Long, dto: EventRequestDto) -> Unit,
    onDeleteEvent: (eventId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Button(
            onClick = onAddClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) { Text("Add Event") }

        when (eventsState) {
            is EventsState.Loading -> LoadingBox()
            is EventsState.Error -> ErrorBox(eventsState.message)
            is EventsState.Success -> {
                val events = eventsState.events
                if (events.isEmpty()) {
                    Text(
                        "No events for ${child.name} ${child.surname}",
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(events) { event ->
                            ChildEventCard(
                                event = event,
                                onEdit = { dto -> onEditEvent(event.id, dto) },
                                onDelete = { onDeleteEvent(event.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddEventDialog(
            onDismiss = onDismissAddDialog,
            onSave = onSaveEvent
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Events Tab - Success")
@Composable
fun EventsTabContentPreview_Success() {
    val sampleChild = Child(
        id = 1L,
        name = "Alice",
        surname = "Johnson",
        email = "alice@example.com",
        phoneNumber = "+123456789",
        dateOfBirth = "2015-06-21",
        active = true
    )

    val sampleEvents = listOf(
        Event(
            id = 1L,
            title = "School Visit",
            description = "Visit the new school building",
            startDateTime = "2025-09-15T10:00",
            endDateTime = "2025-09-15T12:00",
            location = "School",
            childId = 1L
        ),
        Event(
            id = 2L,
            title = "Doctor Appointment",
            description = "Routine check-up",
            startDateTime = "2025-09-16T09:00",
            endDateTime = "2025-09-16T09:30",
            location = "Clinic",
            childId = 1L
        )
    )

    EventsTabContent(
        child = sampleChild,
        eventsState = EventsState.Success(sampleEvents),
        showAddDialog = false,
        onAddClick = {},
        onDismissAddDialog = {},
        onSaveEvent = {},
        onEditEvent = { _, _ -> },
        onDeleteEvent = {}
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Events Tab - Loading")
@Composable
fun EventsTabContentPreview_Loading() {
    val sampleChild = Child(
        id = 1L,
        name = "Alice",
        surname = "Johnson",
        email = "alice@example.com",
        phoneNumber = "+123456789",
        dateOfBirth = "2015-06-21",
        active = true
    )

    EventsTabContent(
        child = sampleChild,
        eventsState = EventsState.Loading,
        showAddDialog = false,
        onAddClick = {},
        onDismissAddDialog = {},
        onSaveEvent = {},
        onEditEvent = { _, _ -> },
        onDeleteEvent = {}
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Events Tab - Error")
@Composable
fun EventsTabContentPreview_Error() {
    val sampleChild = Child(
        id = 1L,
        name = "Alice",
        surname = "Johnson",
        email = "alice@example.com",
        phoneNumber = "+123456789",
        dateOfBirth = "2015-06-21",
        active = true
    )

    EventsTabContent(
        child = sampleChild,
        eventsState = EventsState.Error("Failed to load events"),
        showAddDialog = false,
        onAddClick = {},
        onDismissAddDialog = {},
        onSaveEvent = {},
        onEditEvent = { _, _ -> },
        onDeleteEvent = {}
    )
}
