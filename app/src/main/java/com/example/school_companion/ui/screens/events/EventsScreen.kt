package com.example.school_companion.ui.screens.events

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.school_companion.data.api.EventRequestDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.bar.event.EventTopBar
import com.example.school_companion.ui.dialog.event.AddEventDialog
import com.example.school_companion.ui.dialog.event.EditEventDialog
import com.example.school_companion.ui.navigation.Screen
import com.example.school_companion.ui.section.event.EventWithChildSection
import com.example.school_companion.ui.viewmodel.AuthState
import com.example.school_companion.ui.viewmodel.AuthViewModel
import com.example.school_companion.ui.viewmodel.ChildrenState
import com.example.school_companion.ui.viewmodel.ChildrenViewModel
import com.example.school_companion.ui.viewmodel.EventsViewModel
import com.example.school_companion.ui.viewmodel.EventsWithChildrenState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    eventsViewModel: EventsViewModel = hiltViewModel(),
    childrenViewModel: ChildrenViewModel = hiltViewModel()
) {
    val authToken by authViewModel.authToken.collectAsStateWithLifecycle()
    val eventsState by eventsViewModel.eventsWithChildrenState.collectAsStateWithLifecycle()
    val childrenState by childrenViewModel.childrenState.collectAsStateWithLifecycle()

    val showAddEventDialog = remember { mutableStateOf(false) }
    var selectedChild: Child? by remember { mutableStateOf(null) }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(authToken) {
        authToken?.let { token ->
            eventsViewModel.loadEventsWithChildren(token)
            childrenViewModel.loadChildren(token)
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
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            //Child Filter Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedChild?.let { "${it.name} ${it.surname}" } ?: "All",
                    onValueChange = {},
                    label = { Text("Filter by Child") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("All") },
                        onClick = {
                            selectedChild = null
                            expanded = false
                        }
                    )
                    if (childrenState is ChildrenState.Success) {
                        (childrenState as ChildrenState.Success).children.forEach { child ->
                            DropdownMenuItem(
                                text = { Text("${child.name} ${child.surname}") },
                                onClick = {
                                    selectedChild = child
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Events section
            authToken?.let { token ->
                EventWithChildSection(
                    eventsState = eventsState,
                    selectedChild = selectedChild,
                    authToken = token,
                    navController = navController,
                    eventsViewModel = eventsViewModel
                )
            }
        }
    }

    // Add Event Dialog
    if (showAddEventDialog.value && selectedChild != null && authToken != null) {
        AddEventDialog(
            onDismiss = { showAddEventDialog.value = false },
            onSave = { title, description, start, end, location ->
                eventsViewModel.createEvent(
                    authToken!!,
                    selectedChild!!.id,
                    EventRequestDto(
                        title = title,
                        description = description,
                        startDateTime = start.toString(),
                        endDateTime = end.toString(),
                        location = location
                    )
                )
                showAddEventDialog.value = false
            }
        )
    }
}


