package com.example.school_companion.ui.tab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.MonitoringParam
import com.example.school_companion.ui.section.statistic.ViewByChild
import com.example.school_companion.ui.section.statistic.ViewByParameter
import com.example.school_companion.ui.viewmodel.EntriesState
import com.example.school_companion.ui.viewmodel.UiState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsTabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    selectedChild: Child?,
    selectedParam: MonitoringParam?,
    selectedRange: String,
    childrenState: UiState<List<Child>>,
    entriesState: EntriesState
) {
    val tabs = listOf("View by Parameter", "View by Child")
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.fillMaxWidth()
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = { Text(title, textAlign = TextAlign.Center) }
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    when (selectedTabIndex) {
        0 -> { // View by Parameter
            if (selectedParam == null) {
                Text("Please select a parameter to see statistics charts")
            } else if (childrenState !is UiState.Success || entriesState !is EntriesState.Success) {
                Text("Something went wrong. Try again later.")
            } else {
                val filteredEntries = (entriesState).entryData
                    .filter { e -> e.parameterId == selectedParam.id }
                ViewByParameter(
                    filteredEntries,
                    (childrenState).data,
                    selectedRange
                )
            }
        }

        1 -> { // View by Child
            if (selectedChild == null) {
                Text("Please select a child to see statistics charts")
            } else if (childrenState !is UiState.Success || entriesState !is EntriesState.Success) {
                Text("Something went wrong. Try again later.")
            } else {
                val filteredEntries = (entriesState).entryData
                    .filter { e -> e.childId == selectedChild.id }
                ViewByChild(filteredEntries, selectedChild, selectedRange)
            }
        }
    }
}


