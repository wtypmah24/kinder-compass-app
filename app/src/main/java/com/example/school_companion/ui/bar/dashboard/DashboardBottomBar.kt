package com.example.school_companion.ui.bar.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.school_companion.ui.navigation.Screen

@Composable
fun DashBoardBottomBar(
    navController: NavController,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf(
        Screen.Dashboard to Icons.Default.Dashboard,
        Screen.Children to Icons.Default.People,
        Screen.Events to Icons.Default.Event,
        Screen.Monitoring to Icons.Default.Assessment,
        Screen.Statistics to Icons.Default.BarChart,
        Screen.Assistant to Icons.Default.Assistant
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val threshold = 600.dp

    if (screenWidth > threshold) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            tabs.forEachIndexed { index, pair ->
                val (screen, icon) = pair
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        onTabSelected(index)
                        navController.navigate(screen.route)
                    },
                    icon = { Icon(icon, contentDescription = screen.route) },
                    text = { Text(screen.route) }
                )
            }
        }
    } else {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 8.dp
        ) {
            tabs.forEachIndexed { index, pair ->
                val (screen, icon) = pair
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        onTabSelected(index)
                        navController.navigate(screen.route)
                    },
                    icon = { Icon(icon, contentDescription = screen.route) },
                    text = { Text(screen.route) }
                )
            }
        }
    }
}