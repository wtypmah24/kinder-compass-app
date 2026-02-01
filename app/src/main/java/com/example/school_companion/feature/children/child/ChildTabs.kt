package com.example.school_companion.feature.children.child

import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

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

@Preview(showBackground = true, name = "Child Tabs Preview")
@Composable
fun ChildTabsPreview() {
    ChildTabs(
        selectedTabIndex = 0, // first tab selected
        onTabSelected = {}
    )
}

@Preview(showBackground = true, name = "Child Tabs Preview - Middle Tab Selected")
@Composable
fun ChildTabsPreview_MiddleTab() {
    ChildTabs(
        selectedTabIndex = 2, // third tab selected
        onTabSelected = {}
    )
}
