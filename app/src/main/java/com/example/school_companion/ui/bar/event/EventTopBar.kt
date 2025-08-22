package com.example.school_companion.ui.bar.event

import android.content.Context
import android.widget.Toast
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTopBar(
    navController: NavController,
    selectedChild: Child?,
    context: Context,
    showAddEventDialog: MutableState<Boolean>
) {
    TopAppBar(
        title = { Text("Termine", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = {
                if (selectedChild == null) {
                    Toast.makeText(
                        context,
                        "Please select a child to add new event",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showAddEventDialog.value = true
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    )
}
