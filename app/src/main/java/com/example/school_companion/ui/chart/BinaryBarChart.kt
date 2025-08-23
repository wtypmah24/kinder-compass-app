package com.example.school_companion.ui.chart

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.school_companion.data.model.MonitoringEntry

@Composable
fun BinaryBarChart(entries: List<MonitoringEntry>) {
    val yesCount = entries.count { it.value == "1" || it.value.equals("yes", true) }
    val noCount = entries.size - yesCount
    Text("Yes: $yesCount, No: $noCount")
}