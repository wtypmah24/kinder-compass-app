package com.example.school_companion.ui.tab

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.data.model.Child
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.card.child.ChildSpecialNeedsCard
import com.example.school_companion.ui.dialog.need.AddNeedDialog
import com.example.school_companion.ui.viewmodel.NeedsState
import com.example.school_companion.ui.viewmodel.NeedsViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SpecialNeedsTab(
    child: Child,
    viewModel: NeedsViewModel = hiltViewModel(),
) {
    val needsState by viewModel.needsState.collectAsStateWithLifecycle()
    LaunchedEffect(child) {
        viewModel.loadNeeds(child.id)
    }

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
            is NeedsState.Loading -> LoadingBox()
            is NeedsState.Error -> ErrorBox(message = (needsState as NeedsState.Error).message)
            is NeedsState.Success -> {
                val needs = (needsState as NeedsState.Success).needs

                if (needs.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Keine Special needs für ${child.name}")
                    }
                } else {
                    LazyColumn {
                        items(needs) { need ->
                            ChildSpecialNeedsCard(
                                need = need,
                                onEdit = { dto ->
                                    viewModel.updateNeed(
                                        need.id,
                                        dto,
                                        child.id
                                    )
                                },
                                onDelete = { viewModel.deleteNeed(need.id, child.id) }
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
                viewModel.createNeed(it, child.id)
                showAddDialog = false
            }
        )
    }
}