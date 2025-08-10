package com.example.school_companion.ui.screens.events

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.school_companion.data.api.EventRequestDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.EventWithChild
import com.example.school_companion.ui.dialog.event.AddEventDialog
import com.example.school_companion.ui.dialog.event.EditEventDialog
import com.example.school_companion.ui.navigation.Screen
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

    var showAddEventDialog by remember { mutableStateOf(false) }
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
            TopAppBar(
                title = {
                    Text(
                        text = "Termine",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (selectedChild == null) {
                            Toast.makeText(
                                context,
                                "Please select a child to add new event",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            showAddEventDialog = true
                        }
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Event")
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
            // === Child Filter Dropdown ===
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

            // === Events List ===
            when (eventsState) {
                is EventsWithChildrenState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is EventsWithChildrenState.Success -> {
                    val events = (eventsState as EventsWithChildrenState.Success).events
                    val filteredEvents = if (selectedChild == null) {
                        events
                    } else {
                        events.filter { it.child.id == selectedChild!!.id }
                    }

                    if (filteredEvents.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No events")
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredEvents) { event ->
                                authToken?.let {
                                    EventWithChildCard(
                                        event,
                                        onClick = {
                                            navController.navigate("${Screen.ChildDetail.route}/${event.child.id}")
                                        },
                                        it,
                                        eventsViewModel
                                    )
                                }
                            }
                        }
                    }
                }

                is EventsWithChildrenState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text((eventsState as EventsWithChildrenState.Error).message)
                    }
                }
            }
        }
    }

    // === Add Event Dialog ===
    if (showAddEventDialog && selectedChild != null && authToken != null) {
        AddEventDialog(
            onDismiss = { showAddEventDialog = false },
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
                showAddEventDialog = false
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventWithChildCard(
    event: EventWithChild,
    onClick: () -> Unit,
    token: String,
    eventsViewModel: EventsViewModel
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth(), onClick = onClick, colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = event.event.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = event.event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Start: ${event.event.startDateTime}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "End: ${event.event.endDateTime}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(Modifier.height(12.dp))
                Divider()
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "${event.child.name} ${event.child.surname}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "DOB: ${event.child.dateOfBirth}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            IconButton(onClick = { showEditDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Event",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Event",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    if (showEditDialog) {
        EditEventDialog(
            event = event.event,
            onDismiss = { showEditDialog = false },
            onSave = { updatedEventRequestDto ->
                eventsViewModel.updateEvent(
                    token = token,
                    childId = event.child.id,
                    eventId = event.event.id,
                    event = updatedEventRequestDto
                )
                showEditDialog = false
            }
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete event?") },
            text = { Text("Are you sure you want to delete «${event.event.title}»?") },
            confirmButton = {
                Button(
                    onClick = {
                        eventsViewModel.deleteEvent(token, event.event.id, event.child.id)
                        showDeleteConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

