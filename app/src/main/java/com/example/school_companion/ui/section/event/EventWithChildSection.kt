package com.example.school_companion.ui.section.event

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.card.event.EventWithChildCard
import com.example.school_companion.ui.navigation.Screen
import com.example.school_companion.ui.viewmodel.EventsViewModel
import com.example.school_companion.ui.viewmodel.EventsWithChildrenState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventWithChildSection(
    eventsState: EventsWithChildrenState,
    selectedChild: Child?,
    authToken: String,
    navController: NavController,
    eventsViewModel: EventsViewModel
) {
    when (eventsState) {
        is EventsWithChildrenState.Loading -> LoadingBox()
        is EventsWithChildrenState.Error -> ErrorBox((eventsState).message)

        is EventsWithChildrenState.Success -> {
            val events = (eventsState).events
            val filteredEvents =
                if (selectedChild == null) events else events.filter { it.child.id == selectedChild.id }

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
                        EventWithChildCard(
                            event,
                            onClick = {
                                navController.navigate("${Screen.ChildDetail.route}/${event.child.id}")
                            },
                            authToken,
                            eventsViewModel
                        )
                    }
                }
            }
        }
    }
}