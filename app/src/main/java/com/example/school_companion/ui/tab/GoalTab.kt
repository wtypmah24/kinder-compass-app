package com.example.school_companion.ui.tab

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.data.api.GoalRequestDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.Goal
import com.example.school_companion.data.repository.SessionManager.token
import com.example.school_companion.ui.dialog.goal.AddGoalDialog
import com.example.school_companion.ui.dialog.goal.EditGoalDialog
import com.example.school_companion.ui.dialog.note.EditNoteDialog
import com.example.school_companion.ui.viewmodel.GoalsState
import com.example.school_companion.ui.viewmodel.GoalsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GoalsTab(child: Child, goalsViewModel: GoalsViewModel, token: String) {
    val goalsState by goalsViewModel.goalsState.collectAsStateWithLifecycle()
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
            is GoalsState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is GoalsState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Fehler: ${(goalsState as GoalsState.Error).message}")
                }
            }

            is GoalsState.Success -> {
                val goals = (goalsState as GoalsState.Success).notes

                if (goals.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Keine Goals für ${child.name}")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(goals) { goal ->
                            ChildGoalsCard(goal, goalsViewModel, child, token)
                        }
                    }
                }
            }
        }
    }
    if (showAddDialog) {
        AddGoalDialog(
            onDismiss = { showAddDialog = false },
            onSave = { goal ->
                goalsViewModel.createGoal(
                    token,
                    goal,
                    child.id
                )
                showAddDialog = false
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChildGoalsCard(goal: Goal, goalsViewModel: GoalsViewModel, child: Child, token: String) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = goal.description,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            IconButton(onClick = { showEditDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Special Need",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Special Need",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
    if (showEditDialog) {
        EditGoalDialog(
            goal = goal,
            onDismiss = { showEditDialog = false },
            onSave = { updatedGoalRequestDto ->
                goalsViewModel.updateGoal(
                    token = token,
                    goalId = goal.id,
                    goal = updatedGoalRequestDto,
                    childId = child.id,
                )
                showEditDialog = false
            }
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Goal?") },
            text = { Text("Are you sure you want to delete «${goal.description}»?") },
            confirmButton = {
                Button(
                    onClick = {
                        goalsViewModel.deleteGoal(token, goal.id, child.id)
                        showDeleteConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}