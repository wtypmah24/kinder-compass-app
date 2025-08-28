package com.example.school_companion.ui.tab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.MonitoringParam
import com.example.school_companion.ui.card.monitoring.MonitoringEntryCard
import com.example.school_companion.ui.card.monitoring.MonitoringParamCard
import com.example.school_companion.ui.viewmodel.EntriesState
import com.example.school_companion.ui.viewmodel.MonitoringEntryViewModel
import com.example.school_companion.ui.viewmodel.MonitoringParamViewModel
import com.example.school_companion.ui.viewmodel.ParamsState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonitoringTabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    paramsState: ParamsState,
    entriesState: EntriesState,
    children: List<Child>,
    paramsViewModel: MonitoringParamViewModel,
    entriesViewModel: MonitoringEntryViewModel,
    selectedChild: Child?,
    selectedParam: MonitoringParam?,
    showAddParamDialog: MutableState<Boolean>,
    showAddEntryDialog: () -> Unit
) {
    val tabs = listOf("Monitoring Parameters", "Last Monitoring Entries")

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = { Text(title) }
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    when (selectedTabIndex) {
        0 -> {
            if (paramsState is ParamsState.Success) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { showAddParamDialog.value = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add New Parameter")
                    }
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(paramsState.paramData) { param ->
                            MonitoringParamCard(param, paramsViewModel)
                        }
                    }
                }
            }
        }

        1 -> {
            if (entriesState is EntriesState.Success) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = showAddEntryDialog,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedChild != null && selectedParam != null
                    ) {
                        Text("Add New Entry")
                    }
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(entriesState.entryData) { entry ->
                            val child = children.find { it.id == entry.childId }
                            if (child != null) {
                                MonitoringEntryCard(entry, child, entriesViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
