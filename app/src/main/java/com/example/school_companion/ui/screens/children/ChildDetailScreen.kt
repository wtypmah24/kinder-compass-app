package com.example.school_companion.ui.screens.children

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.bar.dashboard.DashBoardBottomBar
import com.example.school_companion.ui.header.child.ChildHeaderCard
import com.example.school_companion.ui.tab.ChildTabContent
import com.example.school_companion.ui.tab.ChildTabs
import com.example.school_companion.ui.tab.EventsTab
import com.example.school_companion.ui.tab.GoalsTab
import com.example.school_companion.ui.tab.MonitoringEntryTab
import com.example.school_companion.ui.tab.NotesTab
import com.example.school_companion.ui.tab.PhotosTab
import com.example.school_companion.ui.tab.SpecialNeedsTab
import com.example.school_companion.ui.viewmodel.AuthViewModel
import com.example.school_companion.ui.viewmodel.ChildrenViewModel
import com.example.school_companion.ui.viewmodel.EventsViewModel
import com.example.school_companion.ui.viewmodel.GoalsViewModel
import com.example.school_companion.ui.viewmodel.MonitoringEntryViewModel
import com.example.school_companion.ui.viewmodel.NeedsViewModel
import com.example.school_companion.ui.viewmodel.NotesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildDetailScreen(
    navController: NavController,
    childId: Long,
    authViewModel: AuthViewModel,
    childrenViewModel: ChildrenViewModel = hiltViewModel(),
    eventsViewModel: EventsViewModel = hiltViewModel(),
    notesViewModel: NotesViewModel = hiltViewModel(),
    needsViewModel: NeedsViewModel = hiltViewModel(),
    goalsViewModel: GoalsViewModel = hiltViewModel(),
    entriesViewModel: MonitoringEntryViewModel = hiltViewModel(),
) {
    val authToken by authViewModel.authToken.collectAsStateWithLifecycle()
    val selectedChild by childrenViewModel.selectedChild.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedBottomTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(authToken, childId) {
        authToken?.let { token ->
            childrenViewModel.loadChild(token, childId)
            childrenViewModel.loadChildPhotos(token, childId)
            eventsViewModel.loadEventsByChild(token, childId)
            notesViewModel.loadNotes(token, childId)
            needsViewModel.loadNeeds(token, childId)
            goalsViewModel.loadGoals(token, childId)
            entriesViewModel.loadMonitoringEntryData(token, childId)
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
        },
        bottomBar = {
            DashBoardBottomBar(
                navController = navController,
                selectedTabIndex = selectedBottomTabIndex,
                onTabSelected = { selectedBottomTabIndex = it }
            )
        }
    ) { paddingValues ->
        selectedChild?.let { child ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                ChildHeaderCard(child = child)

                ChildTabs(
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { selectedTabIndex = it }
                )

                ChildTabContent(
                    index = selectedTabIndex,
                    child = child,
                    authToken = authToken,
                    eventsViewModel = eventsViewModel,
                    notesViewModel = notesViewModel,
                    needsViewModel = needsViewModel,
                    goalsViewModel = goalsViewModel,
                    entriesViewModel = entriesViewModel,
                    childrenViewModel = childrenViewModel
                )
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
