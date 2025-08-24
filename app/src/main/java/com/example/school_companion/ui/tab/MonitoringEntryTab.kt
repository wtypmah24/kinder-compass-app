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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.api.EntryRequestDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.MonitoringEntry
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.card.child.ChildEntriesCard
import com.example.school_companion.ui.viewmodel.EntriesState
import com.example.school_companion.ui.viewmodel.UiState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonitoringEntryTab(
    child: Child,
    entriesState: UiState<List<MonitoringEntry>>,
    onEditEntry: (Long, EntryRequestDto) -> Unit,
    onDeleteEntry: (MonitoringEntry) -> Unit,
    onAddEntry: () -> Unit,
) {

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
            is UiState.Loading -> LoadingBox()
            is UiState.Error -> ErrorBox(entriesState.message)

            is UiState.Success -> {
                val entries = entriesState.data

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
                                onEdit = { id, dto -> onEditEntry(id, dto) },
                                onDelete = { onDeleteEntry(entry) }
                            )
                        }
                    }
                }
            }
        }
    }
}
