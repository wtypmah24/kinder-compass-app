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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.api.NeedRequestDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.SpecialNeed
import com.example.school_companion.ui.card.child.ChildSpecialNeedsCard
import com.example.school_companion.ui.dialog.need.AddNeedDialog
import com.example.school_companion.ui.viewmodel.UiState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SpecialNeedsTab(
    selectedChild: Child,
    needsState: UiState<List<SpecialNeed>>,
    onAddNeed: (NeedRequestDto) -> Unit,
    onEditNeed: (SpecialNeed, NeedRequestDto) -> Unit,
    onDeleteNeed: (SpecialNeed) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Add new Special Need")
        }

        when (needsState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Fehler: ${(needsState).message}")
                }
            }

            is UiState.Success -> {
                val needs = (needsState).data

                if (needs.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Keine Special needs für ${selectedChild.name}")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(needs) { need ->
                            ChildSpecialNeedsCard(
                                need = need,
                                onEdit = { dto -> onEditNeed(need, dto) },
                                onDelete = { onDeleteNeed(need) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddNeedDialog(
            onDismiss = { showAddDialog = false },
            onSave = {
                onAddNeed(it)
                showAddDialog = false
            }
        )
    }
}