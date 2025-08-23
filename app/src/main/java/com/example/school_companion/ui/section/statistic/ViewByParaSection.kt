package com.example.school_companion.ui.section.statistic

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.MonitoringEntry
import com.example.school_companion.data.model.ScaleType
import com.example.school_companion.ui.chart.BinaryBarChart
import com.example.school_companion.ui.chart.LineChart

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewByParameter(entries: List<MonitoringEntry>, children: List<Child>, range: String) {
    if (entries.isEmpty()) {
        Text("No data available")
        return
    }

    val entriesByChild = entries.groupBy { it.childId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        entriesByChild.forEach { (childId, childEntries) ->
            val child = children.find { it.id == childId }
            val childName = "${child?.name ?: "Unknown"} ${child?.surname ?: ""}".trim()

            Text(
                text = "Chart parameter ${childEntries.first().type} for $childName",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            when (childEntries.first().type) {
                ScaleType.SCALE, ScaleType.QUANTITATIVE -> {
                    LineChart(childEntries, range)
                }

                ScaleType.BINARY -> {
                    BinaryBarChart(childEntries)
                }

                ScaleType.QUALITATIVE -> {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        childEntries.forEach {
                            Text("${it.createdAt}: ${it.value}")
                        }
                    }
                }
            }
        }
    }
}