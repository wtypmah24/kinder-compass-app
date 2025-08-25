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
    authToken: String?,
    navController: NavController
) {

    when (index) {
        0 -> authToken?.let {
            EventsTab(
                child = child,
                token = authToken
            )
        }

        1 -> authToken?.let {
            MonitoringEntryTab(
                child = child,
                token = authToken,
                onAddEntry = { navController.navigate(Screen.Monitoring.route) }
            )
        }

        2 -> authToken?.let {
            NotesTab(
                child = child,
                token = authToken
            )
        }

        3 -> authToken?.let {
            SpecialNeedsTab(
                child = child,
                token = authToken
            )
        }

        4 -> authToken?.let {
            GoalsTab(
                child = child,
                token = authToken
            )
        }

        5 -> authToken?.let {
            PhotosTab(
                child = child,
                token = authToken
            )
        }
    }
}
