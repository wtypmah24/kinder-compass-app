package com.example.school_companion.ui.screens.monitoring

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.MonitoringParam
import com.example.school_companion.ui.bar.dashboard.DashBoardBottomBar
import com.example.school_companion.ui.bar.monitoring.MonitoringTopBar
import com.example.school_companion.ui.card.monitoring.MonitoringSelectorCard
import com.example.school_companion.ui.dialog.entry.AddEntryDialog
import com.example.school_companion.ui.dialog.param.AddParamDialog
import com.example.school_companion.ui.tab.MonitoringTabs
import com.example.school_companion.ui.viewmodel.AuthViewModel
import com.example.school_companion.ui.viewmodel.ChildrenState
import com.example.school_companion.ui.viewmodel.ChildrenViewModel
import com.example.school_companion.ui.viewmodel.MonitoringEntryViewModel
import com.example.school_companion.ui.viewmodel.MonitoringParamViewModel
import com.example.school_companion.ui.viewmodel.ParamsState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonitoringScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    paramsViewModel: MonitoringParamViewModel = hiltViewModel(),
    childrenViewModel: ChildrenViewModel = hiltViewModel(),
    entriesViewModel: MonitoringEntryViewModel = hiltViewModel()
) {
    val authToken by authViewModel.authToken.collectAsStateWithLifecycle()
    val paramsState by paramsViewModel.paramsState.collectAsStateWithLifecycle()
    val entriesState by entriesViewModel.entriesState.collectAsStateWithLifecycle()
    val childrenState by childrenViewModel.childrenState.collectAsStateWithLifecycle()

    var selectedChild: Child? by remember { mutableStateOf(null) }
    var selectedParam: MonitoringParam? by remember { mutableStateOf(null) }
    var showAddEntryDialog by remember { mutableStateOf(false) }
    val showAddParamDialog = remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedBottomTabIndex by remember { mutableIntStateOf(0) }


    LaunchedEffect(authToken) {
        authToken?.let { token ->
            paramsViewModel.loadMonitoringParamData(token)
            entriesViewModel.loadMonitoringEntryByCompanion(token)
            childrenViewModel.loadChildren(token)
        }
    }

    Scaffold(
        topBar = {
            MonitoringTopBar(
                navController = navController,
                selectedChild = selectedChild,
                selectedParam = selectedParam,
                showAddParamDialog = showAddParamDialog
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

        if (childrenState is ChildrenState.Success && paramsState is ParamsState.Success) {
            MonitoringSelectorCard(
                children = (childrenState as ChildrenState.Success).children,
                selectedChild = selectedChild,
                onChildSelected = { selectedChild = it },
                params = (paramsState as ParamsState.Success).paramData,
                selectedParam = selectedParam,
                onParamSelected = { selectedParam = it },
                paddingValues = paddingValues
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        MonitoringTabs(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it },
            paramsState = paramsState,
            entriesState = entriesState,
            children = (childrenState as? ChildrenState.Success)?.children.orEmpty(),
            authToken = authToken,
            paramsViewModel = paramsViewModel,
            entriesViewModel = entriesViewModel,
            selectedChild = selectedChild,
            selectedParam = selectedParam,
            showAddParamDialog = showAddParamDialog,
            showAddEntryDialog = { showAddEntryDialog = true }
        )

    }

    if (showAddParamDialog.value && authToken != null) {
        AddParamDialog(
            onDismiss = { showAddParamDialog.value = false },
            onSave = { param ->
                paramsViewModel.createMonitoringParam(
                    authToken!!,
                    param
                )
            }
        )
    }
    if (showAddEntryDialog && authToken != null && selectedParam != null && selectedChild != null) {
        AddEntryDialog(
            param = selectedParam!!,
            onDismiss = { showAddEntryDialog = false },
            onSave = { entry ->
                entriesViewModel.createMonitoringEntry(
                    authToken!!,
                    entry,
                    selectedChild!!.id,
                    selectedParam!!.id
                )
            }
        )
    }
}

