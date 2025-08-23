package com.example.school_companion.ui.screens.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.school_companion.ui.bar.dashboard.DashBoardBottomBar
import com.example.school_companion.ui.bar.dashboard.DashboardTopBar
import com.example.school_companion.ui.card.dashboard.WelcomeCompanionCard
import com.example.school_companion.ui.navigation.Screen
import com.example.school_companion.ui.section.action.QuickActionSection
import com.example.school_companion.ui.section.children.ChildrenSection
import com.example.school_companion.ui.section.event.EventsSection
import com.example.school_companion.ui.viewmodel.AuthViewModel
import com.example.school_companion.ui.viewmodel.ChildrenViewModel
import com.example.school_companion.ui.viewmodel.EventsViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    childrenViewModel: ChildrenViewModel = hiltViewModel(),
    eventsViewModel: EventsViewModel = hiltViewModel()
) {
    val currentUser by authViewModel.currentCompanion.collectAsStateWithLifecycle()
    val authToken by authViewModel.authToken.collectAsStateWithLifecycle()
    val childrenState by childrenViewModel.childrenState.collectAsStateWithLifecycle()
    val eventsState by eventsViewModel.eventsState.collectAsStateWithLifecycle()

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(authToken) {
        if (!authToken.isNullOrBlank()) {
            childrenViewModel.loadChildren(authToken!!)
            eventsViewModel.loadEventsByCompanion(authToken!!)
        }
    }

    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = {
            DashBoardBottomBar(
                navController = navController,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )
        }

    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome Section
            item {
                currentUser?.let { WelcomeCompanionCard(currentUser = it) }
            }

            // Quick Actions
            item {
                QuickActionSection(navController = navController)
            }

            // Assigned Children
            item {
                ChildrenSection(
                    childrenState = childrenState,
                    childrenViewModel = childrenViewModel,
                    navController = navController,
                    maxItems = 3,
                    onShowAllClick = { navController.navigate(Screen.Children.route) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Upcoming Events
            item {
                EventsSection(eventsState = eventsState, navController = navController)
            }
        }
    }
}
