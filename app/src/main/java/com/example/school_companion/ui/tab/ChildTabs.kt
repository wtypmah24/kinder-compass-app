package com.example.school_companion.ui.tab

import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ChildTabs(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabTitles = listOf(
        "Events",
        "Monitoring",
        "Notizen",
        "Besondere Bedürfnisse",
        "Ziele",
        "Fotos"
    )

    ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = { Text(title) }
            )
        }
    }
}