package com.example.school_companion.ui.section.action

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.school_companion.ui.card.dashboard.QuickActionCard
import com.example.school_companion.ui.navigation.Screen

@Composable
fun QuickActionSection(navController: NavController){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Schnellzugriff",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                title = "Monitoring",
                icon = Icons.Default.Assessment,
                onClick = { navController.navigate(Screen.Monitoring.route) },
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                title = "Termine",
                icon = Icons.Default.Event,
                onClick = { navController.navigate(Screen.Events.route) },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                title = "Kinder",
                icon = Icons.Default.People,
                onClick = { navController.navigate(Screen.Children.route) },
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                title = "Statistiken",
                icon = Icons.Default.BarChart,
                onClick = { navController.navigate(Screen.Statistics.route) },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                title = "AI Assistant",
                icon = Icons.Default.Assistant,
                onClick = { navController.navigate(Screen.Assistant.route) },
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                title = "Planner",
                icon = Icons.Default.EditCalendar,
                onClick = { /* TODO */ },
                modifier = Modifier.weight(1f)
            )
        }
    }
}