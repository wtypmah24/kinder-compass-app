package com.example.school_companion.ui.screens.children

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.school_companion.ui.viewmodel.AuthViewModel
import com.example.school_companion.ui.viewmodel.ChildrenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildDetailScreen(
    navController: NavController,
    childId: String,
    authViewModel: AuthViewModel = hiltViewModel(),
    childrenViewModel: ChildrenViewModel = hiltViewModel()
) {
    val authToken by authViewModel.authToken.collectAsStateWithLifecycle()
    val selectedChild by childrenViewModel.selectedChild.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(authToken, childId) {
        authToken?.let { token ->
            childrenViewModel.loadChild(token, childId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = selectedChild?.let { "${it.name} ${it.surname}" }
                            ?: "Kind Details",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        selectedChild?.let { child ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Child Info Header
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Child photo placeholder
                        Card(
                            modifier = Modifier.size(80.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                        ) {
                            Text(
                                text = "${child.name} ${child.surname}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Geboren: ${child.dateOfBirth}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
//                            if (child.diagnosis != null) {
//                                Text(
//                                    text = "Diagnose: ${child.diagnosis}",
//                                    fontSize = 12.sp,
//                                    color = MaterialTheme.colorScheme.primary,
//                                    modifier = Modifier.padding(top = 4.dp)
//                                )
//                            }
                        }
                    }
                }

                // Tabs
                TabRow(selectedTabIndex = selectedTabIndex) {
                    listOf(
                        "Events",
                        "Monitoring",
                        "Notizen",
                        "Besondere Bedürfnisse",
                        "Ziele",
                        "Fotos"
                    ).forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }

                // Tab Content
                when (selectedTabIndex) {
                    0 -> EventsTab(childId = childId)
                    1 -> MonitoringTab(childId = childId)
                    2 -> NotesTab(childId = childId)
                    3 -> SpecialNeedsTab(child = child)
                    4 -> GoalsTab(child = child)
                    5 -> PhotosTab(childId = childId)
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun EventsTab(childId: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Events für Kind $childId")
    }
}

@Composable
fun MonitoringTab(childId: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Monitoring für Kind $childId")
    }
}

@Composable
fun NotesTab(childId: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Notizen für Kind $childId")
    }
}

@Composable
fun SpecialNeedsTab(child: com.example.school_companion.data.model.Child) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Besondere Bedürfnisse",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

//        if (child.specialNeeds.isNotEmpty()) {
//            child.specialNeeds.forEach { need ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp)
//                ) {
//                    Text(
//                        text = need,
//                        modifier = Modifier.padding(16.dp)
//                    )
//                }
//            }
//        } else {
//            Text("Keine besonderen Bedürfnisse dokumentiert")
//        }

//        if (child.supportNeeds != null) {
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        text = "Unterstützungsbedarf",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                    Text(
//                        text = child.supportNeeds,
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                }
//            }
//        }
    }
}

@Composable
fun GoalsTab(child: com.example.school_companion.data.model.Child) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Entwicklungsziele",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

//        if (child.goals.isNotEmpty()) {
//            child.goals.forEach { goal ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp)
//                ) {
//                    Column(
//                        modifier = Modifier.padding(16.dp)
//                    ) {
//                        Text(
//                            text = goal.title,
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Medium
//                        )
//                        Text(
//                            text = goal.description,
//                            modifier = Modifier.padding(top = 4.dp)
//                        )
//                        LinearProgressIndicator(
//                            progress = goal.progress / 100f,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(top = 8.dp)
//                        )
//                        Text(
//                            text = "Fortschritt: ${goal.progress}%",
//                            fontSize = 12.sp,
//                            modifier = Modifier.padding(top = 4.dp)
//                        )
//                    }
//                }
//            }
//        } else {
//            Text("Keine Ziele definiert")
//        }
    }
}

@Composable
fun PhotosTab(childId: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Fotos für Kind $childId")
    }
}
