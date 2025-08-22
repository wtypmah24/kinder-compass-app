package com.example.school_companion.ui.tab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.viewmodel.ChildrenViewModel
import com.example.school_companion.ui.viewmodel.EventsViewModel
import com.example.school_companion.ui.viewmodel.GoalsViewModel
import com.example.school_companion.ui.viewmodel.MonitoringEntryViewModel
import com.example.school_companion.ui.viewmodel.NeedsViewModel
import com.example.school_companion.ui.viewmodel.NotesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChildTabContent(
    index: Int,
    child: Child,
    authToken: String?,
    eventsViewModel: EventsViewModel,
    notesViewModel: NotesViewModel,
    needsViewModel: NeedsViewModel,
    goalsViewModel: GoalsViewModel,
    entriesViewModel: MonitoringEntryViewModel,
    childrenViewModel: ChildrenViewModel
) {
    when (index) {
        0 -> authToken?.let {
            EventsTab(child, it, eventsViewModel)
        }
        1 -> authToken?.let {
            MonitoringEntryTab(child, entriesViewModel, it)
        }
        2 -> authToken?.let {
            NotesTab(child, it, notesViewModel)
        }
        3 -> authToken?.let {
            SpecialNeedsTab(child, needsViewModel, it)
        }
        4 -> authToken?.let {
            GoalsTab(child, goalsViewModel, it)
        }
        5 -> authToken?.let {
            PhotosTab(child, childrenViewModel, it)
        }
    }
}