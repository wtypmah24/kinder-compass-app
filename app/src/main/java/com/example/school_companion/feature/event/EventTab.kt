package com.example.school_companion.feature.event

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.data.model.Child

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

    EventsTabContent(
        child = child,
        eventsState = eventsState,
        showAddDialog = showAddDialog,
        onAddClick = { showAddDialog = true },
        onDismissAddDialog = { showAddDialog = false },
        onSaveEvent = { eventDto ->
            viewModel.createEvent(child.id, eventDto)
            showAddDialog = false
        },
        onEditEvent = { eventId, dto -> viewModel.updateEvent(child.id, eventId, dto) },
        onDeleteEvent = { eventId -> viewModel.deleteEvent(eventId, child.id) },
        modifier = Modifier.fillMaxSize()
    )
}