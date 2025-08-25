package com.example.school_companion.ui.screens.statistics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.MonitoringParam
import com.example.school_companion.ui.bar.dashboard.DashBoardBottomBar
import com.example.school_companion.ui.card.statistic.StatisticsSelectorCard
import com.example.school_companion.ui.card.statistic.StatisticsSummaryCard
import com.example.school_companion.ui.tab.StatisticsTabs
import com.example.school_companion.ui.viewmodel.AuthViewModel
import com.example.school_companion.ui.viewmodel.ChildrenState
import com.example.school_companion.ui.viewmodel.ChildrenViewModel
import com.example.school_companion.ui.viewmodel.EntriesState
import com.example.school_companion.ui.viewmodel.MonitoringEntryViewModel
import com.example.school_companion.ui.viewmodel.MonitoringParamViewModel
import com.example.school_companion.ui.viewmodel.ParamsState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    paramsViewModel: MonitoringParamViewModel = hiltViewModel(),
    entriesViewModel: MonitoringEntryViewModel = hiltViewModel(),
    childrenViewModel: ChildrenViewModel = hiltViewModel(),
) {
    val authToken by authViewModel.authToken.collectAsStateWithLifecycle()
    val paramsState by paramsViewModel.paramsState.collectAsStateWithLifecycle()
    val entriesState by entriesViewModel.entriesState.collectAsStateWithLifecycle()
    val childrenState by childrenViewModel.childrenState.collectAsStateWithLifecycle()

    var selectedChild by remember { mutableStateOf<Child?>(null) }
    var selectedParam by remember { mutableStateOf<MonitoringParam?>(null) }
    var selectedRange by remember { mutableStateOf("Last 7 days") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedBottomTabIndex by remember { mutableIntStateOf(0) }

    val timeRanges = listOf("Last Day", "Last 7 Days", "Last 30 Days", "Last 90 Days")

    LaunchedEffect(authToken) {
        authToken?.let { token ->
            paramsViewModel.loadMonitoringParamData(token)
            entriesViewModel.loadMonitoringEntryByCompanion(token)
            childrenViewModel.loadChildren(token)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            DashBoardBottomBar(
                selectedTabIndex = selectedBottomTabIndex,
                onTabSelected = { selectedBottomTabIndex = it },
                onTabNavigate = { screen ->
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (childrenState is ChildrenState.Success && paramsState is ParamsState.Success) {
                StatisticsSelectorCard(
                    children = (childrenState as ChildrenState.Success).children,
                    selectedChild = selectedChild,
                    onChildSelected = { selectedChild = it },
                    params = (paramsState as ParamsState.Success).paramData,
                    selectedParam = selectedParam,
                    onParamSelected = { selectedParam = it },
                    ranges = timeRanges,
                    selectedRange = selectedRange,
                    onRangeSelected = { if (it != null) selectedRange = it }
                )
            }

            if (entriesState is EntriesState.Success) {
                val entries = (entriesState as EntriesState.Success).entryData
                StatisticsSummaryCard(entries)
            }

            StatisticsTabs(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it },
                selectedChild = selectedChild,
                selectedParam = selectedParam,
                selectedRange = selectedRange,
                childrenState = childrenState,
                entriesState = entriesState
            )
        }
    }
}

