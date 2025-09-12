package com.example.school_companion.feature.event

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.data.model.Child
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.ui.bar.DashBoardBottomBar
import com.example.school_companion.ui.dropdown.DropdownMenuWrapper
import com.example.school_companion.ui.util.UiState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventsScreen(
    onNavigate: NavigateToWithArgs,
    childrenState: UiState<List<Child>>,
    eventsViewModel: EventsViewModel,
) {
    val eventsState by eventsViewModel.eventsWithChildrenState.collectAsStateWithLifecycle()

    val showAddEventDialog = remember { mutableStateOf(false) }
    var selectedChild: Child? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    var selectedBottomTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        eventsViewModel.loadEventsWithChildren()
    }

    Scaffold(
        topBar = {
            EventTopBar(
                onNavigate = onNavigate,
                selectedChild = selectedChild,
                context = context,
                showAddEventDialog = showAddEventDialog
            )
        },
        bottomBar = {
            DashBoardBottomBar(
                selectedTabIndex = selectedBottomTabIndex,
                onTabSelected = { selectedBottomTabIndex = it },
                onTabNavigate = { screen ->
                    onNavigate(screen, null)
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            //Child Filter Dropdown
            DropdownMenuWrapper(
                items = if (childrenState is UiState.Success) childrenState.data else emptyList(),
                selectedItem = selectedChild,
                onItemSelected = { selectedChild = it },
                itemToString = { "${it.name} ${it.surname}" },
                placeholder = "All",
                extraItem = null,
                extraItemText = "All"
            )

            Spacer(Modifier.height(16.dp))

            // Events section
            EventWithChildSection(
                eventsState = eventsState,
                selectedChild = selectedChild,
                onNavigate = onNavigate,
                { childId, eventId, updatedEvent ->
                    eventsViewModel.updateEvent(
                        childId,
                        eventId,
                        updatedEvent
                    )
                },
                { childId, eventId -> eventsViewModel.deleteEvent(eventId, childId) }

            )
        }
    }

    // Add Event Dialog
    if (showAddEventDialog.value && selectedChild != null) {
        AddEventDialog(
            onDismiss = { showAddEventDialog.value = false },
            onSave = { dto ->
                eventsViewModel.createEvent(
                    selectedChild!!.id,
                    dto
                )
                showAddEventDialog.value = false
            }
        )
    }
}


