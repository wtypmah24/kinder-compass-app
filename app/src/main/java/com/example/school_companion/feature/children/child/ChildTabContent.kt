package com.example.school_companion.feature.children.child

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.school_companion.data.model.Child
import com.example.school_companion.feature.event.EventsTab
import com.example.school_companion.feature.foto.PhotosTab
import com.example.school_companion.feature.goal.GoalsTab
import com.example.school_companion.feature.monitoring.entry.MonitoringEntryTab
import com.example.school_companion.feature.need.SpecialNeedsTab
import com.example.school_companion.feature.note.NotesTab
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.navigation.Screen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChildTabContent(
    index: Int,
    child: Child,
    onNavigate: NavigateToWithArgs
) {

    when (index) {
        0 -> EventsTab(child = child)

        1 -> MonitoringEntryTab(
            child = child,
            onAddEntry = { onNavigate(Screen.Monitoring, null) }
        )

        2 -> NotesTab(child = child)

        3 -> SpecialNeedsTab(child = child)

        4 -> GoalsTab(child = child)

        5 ->
            PhotosTab(
                child = child,
            )
    }
}
