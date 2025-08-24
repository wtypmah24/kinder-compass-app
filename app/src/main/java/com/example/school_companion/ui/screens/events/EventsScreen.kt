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
import com.example.school_companion.ui.viewmodel.AuthViewModel
import com.example.school_companion.ui.viewmodel.ChildDetailViewModel
import com.example.school_companion.ui.viewmodel.UiState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventsScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    viewModel: ChildDetailViewModel = hiltViewModel(),
) {
    val authToken by authViewModel.authToken.collectAsStateWithLifecycle()
    val eventsState by viewModel.eventsWithChildren.collectAsStateWithLifecycle()
    val childrenState by viewModel.children.collectAsStateWithLifecycle()

    val showAddEventDialog = remember { mutableStateOf(false) }
    var selectedChild: Child? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    var selectedBottomTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(authToken) {
        authToken?.let { token ->
            viewModel.loadEventsWithChildren(token)
            viewModel.loadChildren(token)
        }
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
            authToken?.let { token ->
                EventWithChildSection(
                    eventsState = eventsState,
                    selectedChild = selectedChild,
                    authToken = token,
                    navController = navController,
                    eventsViewModel = viewModel
                )
            }
        }
    }

    // Add Event Dialog
    if (showAddEventDialog.value && selectedChild != null && authToken != null) {
        AddEventDialog(
            onDismiss = { showAddEventDialog.value = false },
            onSave = { dto ->
                viewModel.createEvent(
                    authToken!!,
                    selectedChild!!.id,
                    dto
                )
                showAddEventDialog.value = false
            }
        )
    }
}


