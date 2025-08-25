package com.example.school_companion.ui.tab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.card.child.ChildEntriesCard
import com.example.school_companion.ui.viewmodel.EntriesState
import com.example.school_companion.ui.viewmodel.MonitoringEntryViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonitoringEntryTab(
    child: Child,
    token: String,
    viewModel: MonitoringEntryViewModel = hiltViewModel(),
    onAddEntry: () -> Unit,
) {
    val entriesState by viewModel.entriesState.collectAsStateWithLifecycle()

    LaunchedEffect(token, child) {
        viewModel.loadMonitoringEntryData(token, child.id)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { onAddEntry() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Add new Monitoring Entry")
        }
        when (entriesState) {
            is EntriesState.Loading -> LoadingBox()
            is EntriesState.Error -> ErrorBox((entriesState as EntriesState.Error).message)

            is EntriesState.Success -> {
                val entries = (entriesState as EntriesState.Success).entryData

                if (entries.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Keine Monitoring entries für ${child.name}")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(entries) { entry ->
                            ChildEntriesCard(
                                entry = entry,
                                onEdit = { id, dto ->
                                    viewModel.updateMonitoringEntry(
                                        token,
                                        dto,
                                        child.id,
                                        id
                                    )
                                },
                                onDelete = {
                                    viewModel.deleteMonitoringEntry(
                                        token,
                                        entry.id,
                                        child.id
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
