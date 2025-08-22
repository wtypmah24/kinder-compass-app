package com.example.school_companion.ui.bar.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.school_companion.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = "Dashboard",
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                Icon(Icons.Default.Person, contentDescription = "Profile")
            }
            IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
    )
}