package com.example.school_companion.feature.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.data.model.Companion
import com.example.school_companion.feature.children.ChildrenSection
import com.example.school_companion.feature.children.ChildrenViewModel
import com.example.school_companion.feature.event.EventsSection
import com.example.school_companion.feature.event.EventsState
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.navigation.Screen
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.util.ChildActionHandler
import com.example.school_companion.ui.util.UiState
import com.example.school_companion.ui.util.onState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    onNavigate: NavigateToWithArgs,
    currentUserState: UiState<Companion>,
    eventsState: EventsState,
    quickActions: List<QuickAction>,
    childrenViewModel: ChildrenViewModel,
) {

    val childrenState by childrenViewModel.childrenState.collectAsStateWithLifecycle()

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Welcome Section
            item {
                currentUserState.onState(
                    onLoading = { LoadingBox() },
                    onSuccess = { WelcomeCompanionCard(currentUser = it) },
                    onError = { msg -> ErrorBox(message = msg) }
                )
            }

            // Quick Actions
            item {
                QuickActionSection(actions = quickActions)
            }

            // Assigned Children
            item {
                ChildrenSection(
                    childrenState = childrenState,
                    maxItems = 3,
                    onChildAction = { child, action ->
                        ChildActionHandler.handle(
                            child,
                            action,
                            onNavigate,
                            onDeleteChild = { id -> childrenViewModel.deleteChild(id) },
                            onEditChild = { id, updatedChild ->
                                childrenViewModel.updateChild(
                                    id,
                                    updatedChild
                                )
                            },
                            onSetSelectedChild = { selectedChild ->
                                childrenViewModel.setSelectedChild(selectedChild)
                            }
                        )

                    },
                    onShowAllClick = {
                        onNavigate(Screen.Children, null)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Upcoming Events
            item {
                EventsSection(eventsState = eventsState, onNavigate = onNavigate)
            }
        }
    }
}
