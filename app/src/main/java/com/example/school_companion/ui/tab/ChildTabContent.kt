package com.example.school_companion.ui.tab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.navigation.Screen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChildTabContent(
    index: Int,
    child: Child,
    navController: NavController
) {

    when (index) {
        0 -> EventsTab(child = child)

        1 -> MonitoringEntryTab(
            child = child,
            onAddEntry = { navController.navigate(Screen.Monitoring.route) }
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
