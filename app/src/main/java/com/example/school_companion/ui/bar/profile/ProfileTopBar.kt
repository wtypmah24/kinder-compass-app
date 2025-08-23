package com.example.school_companion.ui.bar.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
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
import com.example.school_companion.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(
    navController: NavController,
) {
    TopAppBar(title = {
        Text(
            text = "Profile", fontWeight = FontWeight.Bold
        )
    }, navigationIcon = {
        IconButton(onClick = { navController.navigateUp() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    }, actions = {
        IconButton(onClick = { navController.navigate(Screen.Login.route) }) {
            Icon(Icons.Default.Logout, contentDescription = "Logout")
        }
    })
}

