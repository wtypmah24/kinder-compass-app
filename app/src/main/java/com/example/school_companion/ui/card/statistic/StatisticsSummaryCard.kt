package com.example.school_companion.ui.card.statistic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.model.MonitoringEntry

@Composable
fun StatisticsSummaryCard(entries: List<MonitoringEntry>) {
    val totalEntries = entries.size
    val activeChildren = entries.map { it.childId }.distinct().count()
    val parametersTracked = entries.map { it.parameterId }.distinct().count()
    val avgPerDay = if (entries.isNotEmpty()) totalEntries / 30 else 0 // Пример

    Card(
        modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Statistics Summary", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Total Entries: $totalEntries")
            Text("Active Children: $activeChildren")
            Text("Parameters Tracked: $parametersTracked")
            Text("Average per Day: $avgPerDay")
        }
    }
}