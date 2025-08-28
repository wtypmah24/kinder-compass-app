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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.card.child.ChildEventCard
import com.example.school_companion.ui.dialog.event.AddEventDialog
import com.example.school_companion.ui.viewmodel.EventsState
import com.example.school_companion.ui.viewmodel.EventsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventsTab(
    child: Child,
    viewModel: EventsViewModel = hiltViewModel()
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val eventsState by viewModel.eventsState.collectAsStateWithLifecycle()

    LaunchedEffect(child) {
        viewModel.loadEventsByChild(child.id)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) { Text("Add Event") }

        when (eventsState) {
            is EventsState.Loading -> LoadingBox()
            is EventsState.Error -> ErrorBox((eventsState as EventsState.Error).message)
            is EventsState.Success -> {
                val events = (eventsState as EventsState.Success).events
                if (events.isEmpty()) {
                    Text("Keine Events für Child ${child.name} ${child.surname}")
                } else {
                    LazyColumn {
                        items(events) { event ->
                            ChildEventCard(
                                event = event,
                                onEdit = { dto ->
                                    viewModel.updateEvent(
                                        child.id,
                                        event.id,
                                        dto
                                    )
                                },
                                onDelete = { viewModel.deleteEvent(event.id, child.id) }
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
                viewModel.createEvent(child.id, eventDto)
                showAddDialog = false
            }
        )
    }
}
