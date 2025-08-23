package com.example.school_companion.ui.bar.monitoring

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.MonitoringParam

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonitoringTopBar(
    navController: NavController,
    selectedChild: Child?,
    selectedParam: MonitoringParam?,
    showAddParamDialog: MutableState<Boolean>
) {
    TopAppBar(
        title = { Text("Monitoring", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(
                onClick = { showAddParamDialog.value = true },
                enabled = selectedChild != null && selectedParam != null
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Monitoring Entry")
            }
        }
    )
}

