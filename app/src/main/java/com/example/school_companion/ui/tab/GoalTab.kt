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
import com.example.school_companion.ui.card.child.ChildGoalsCard
import com.example.school_companion.ui.dialog.goal.AddGoalDialog
import com.example.school_companion.ui.viewmodel.GoalsState
import com.example.school_companion.ui.viewmodel.GoalsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GoalsTab(
    child: Child,
    token: String,
    viewModel: GoalsViewModel = hiltViewModel(),
) {
    val goalsState by viewModel.goalsState.collectAsStateWithLifecycle()
    LaunchedEffect(token, child) {
        viewModel.loadGoals(token, child.id)
    }

    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Add new Goal")
        }

        when (goalsState) {
            is GoalsState.Loading -> LoadingBox()
            is GoalsState.Error -> ErrorBox(message = (goalsState as GoalsState.Error).message)

            is GoalsState.Success -> {
                val goals = (goalsState as GoalsState.Success).goals

                if (goals.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Keine Goals für ${child.name} ${child.surname}")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(goals) { goal ->
                            ChildGoalsCard(
                                goal = goal,
                                onEdit = { dto ->
                                    viewModel.updateGoal(
                                        token,
                                        goal.id,
                                        dto,
                                        child.id
                                    )
                                },
                                onDelete = { viewModel.deleteGoal(token, goal.id, child.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddGoalDialog(
            onDismiss = { showAddDialog = false },
            onSave = {
                viewModel.createGoal(token, it, child.id)
                showAddDialog = false
            }
        )
    }
}