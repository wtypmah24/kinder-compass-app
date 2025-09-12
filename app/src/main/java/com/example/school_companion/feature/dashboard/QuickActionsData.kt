package com.example.school_companion.feature.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.People
import androidx.navigation.NavController
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.navigation.Screen

object QuickActionsData {
    fun getQuickActions(onNavigate: NavigateToWithArgs): List<QuickAction> = listOf(
        QuickAction(
            "Monitoring",
            Icons.Default.Assessment
        ) { onNavigate(Screen.Monitoring, null) },
        QuickAction(
            "Termine",
            Icons.Default.Event
        ) { onNavigate(Screen.Events, null) },
        QuickAction(
            "Kinder",
            Icons.Default.People
        ) { onNavigate(Screen.Children, null) },
        QuickAction(
            "Statistiken",
            Icons.Default.BarChart
        ) { onNavigate(Screen.Statistics, null) },
        QuickAction(
            "AI Assistant",
            Icons.Default.Assistant
        ) { onNavigate(Screen.Assistant, null) },
        QuickAction(
            "Planner",
            Icons.Default.EditCalendar
        ) { /* TODO */ }
    )
}
