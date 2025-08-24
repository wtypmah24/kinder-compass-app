package com.example.school_companion.ui.section.event

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.school_companion.data.model.Event
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.card.event.EventCard
import com.example.school_companion.ui.navigation.Screen
import com.example.school_companion.ui.viewmodel.UiState

@Composable
fun EventsSection(
    eventsState: UiState<List<Event>>,
    navController: NavController
) {
    Column {
        Text(
            text = "Anstehende Termine",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        when (eventsState) {
            is UiState.Loading -> LoadingBox()
            is UiState.Error -> ErrorBox(eventsState.message)

            is UiState.Success -> {
                val events =
                    (eventsState).data
                if (events.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Event,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Keine anstehenden Termine",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                } else {
                    (events.take(3)).forEach { event ->
                        EventCard(event = event)
                    }
                    if (events.size > 3) {
                        TextButton(
                            onClick = { navController.navigate(Screen.Events.route) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Alle ${events.size} Termine anzeigen")
                        }

                    }
                }
            }
        }
    }
}

