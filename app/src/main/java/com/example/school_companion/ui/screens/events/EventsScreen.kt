package com.example.school_companion.ui.screens.events

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.bar.dashboard.DashBoardBottomBar
import com.example.school_companion.ui.bar.event.EventTopBar
import com.example.school_companion.ui.dialog.event.AddEventDialog
import com.example.school_companion.ui.dropdown.DropdownMenuWrapper
import com.example.school_companion.ui.section.event.EventWithChildSection
import com.example.school_companion.ui.viewmodel.ChildrenViewModel
import com.example.school_companion.ui.viewmodel.EventsViewModel
import com.example.school_companion.ui.viewmodel.UiState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventsScreen(
    navController: NavController,
    childrenViewModel: ChildrenViewModel = hiltViewModel(),
    eventsViewModel: EventsViewModel = hiltViewModel(),
) {
    val eventsState by eventsViewModel.eventsWithChildrenState.collectAsStateWithLifecycle()
    val childrenState by childrenViewModel.childrenState.collectAsStateWithLifecycle()

    val showAddEventDialog = remember { mutableStateOf(false) }
    var selectedChild: Child? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    var selectedBottomTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        eventsViewModel.loadEventsWithChildren()
        childrenViewModel.loadChildren()
    }

    Scaffold(
        topBar = {
            EventTopBar(
                navController = navController,
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
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
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
                items = if (childrenState is UiState.Success) (childrenState as UiState.Success).data else emptyList(),
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
                navController = navController,
                eventsViewModel = eventsViewModel
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


