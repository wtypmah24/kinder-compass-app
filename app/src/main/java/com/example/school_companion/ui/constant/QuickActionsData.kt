package com.example.school_companion.ui.constant

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.People
import androidx.navigation.NavController
import com.example.school_companion.ui.navigation.Screen
import com.example.school_companion.ui.section.action.QuickAction

object QuickActionsData {
    fun getQuickActions(navController: NavController): List<QuickAction> = listOf(
        QuickAction(
            "Monitoring",
            Icons.Default.Assessment
        ) { navController.navigate(Screen.Monitoring.route) },
        QuickAction("Termine", Icons.Default.Event) { navController.navigate(Screen.Events.route) },
        QuickAction(
            "Kinder",
            Icons.Default.People
        ) { navController.navigate(Screen.Children.route) },
        QuickAction(
            "Statistiken",
            Icons.Default.BarChart
        ) { navController.navigate(Screen.Statistics.route) },
        QuickAction(
            "AI Assistant",
            Icons.Default.Assistant
        ) { navController.navigate(Screen.Assistant.route) },
        QuickAction("Planner", Icons.Default.EditCalendar) { /* TODO */ }
    )
}
