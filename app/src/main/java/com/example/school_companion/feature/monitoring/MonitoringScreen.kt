package com.example.school_companion.feature.monitoring

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.MonitoringParam
import com.example.school_companion.feature.monitoring.entry.AddEntryDialog
import com.example.school_companion.feature.monitoring.entry.MonitoringEntryViewModel
import com.example.school_companion.feature.monitoring.param.AddParamDialog
import com.example.school_companion.feature.monitoring.param.MonitoringParamViewModel
import com.example.school_companion.feature.monitoring.param.ParamsState
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.ui.util.UiState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonitoringScreen(
    onNavigate: NavigateToWithArgs,
    paramsViewModel: MonitoringParamViewModel,
    entriesViewModel: MonitoringEntryViewModel,
    childrenState: UiState<List<Child>>
) {
    val paramsState by paramsViewModel.paramsState.collectAsStateWithLifecycle()
    val entriesState by entriesViewModel.entriesState.collectAsStateWithLifecycle()

    var selectedChild: Child? by remember { mutableStateOf(null) }
    var selectedParam: MonitoringParam? by remember { mutableStateOf(null) }
    var showAddEntryDialog by remember { mutableStateOf(false) }
    val showAddParamDialog = remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            MonitoringTopBar(
                onBack = { onNavigate(null, null) },
                showAddParamDialog = showAddParamDialog
            )
        }
    ) { paddingValues ->

        if (childrenState is UiState.Success && paramsState is ParamsState.Success) {
            MonitoringSelectorCard(
                children = childrenState.data,
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
            children = (childrenState as? UiState.Success)?.data.orEmpty(),
            paramsViewModel = paramsViewModel,
            entriesViewModel = entriesViewModel,
            selectedChild = selectedChild,
            selectedParam = selectedParam,
            showAddParamDialog = showAddParamDialog,
            showAddEntryDialog = { showAddEntryDialog = true }
        )

    }

    if (showAddParamDialog.value) {
        AddParamDialog(
            onDismiss = { showAddParamDialog.value = false },
            onSave = { param ->
                paramsViewModel.createMonitoringParam(
                    param
                )
            }
        )
    }
    if (showAddEntryDialog && selectedParam != null && selectedChild != null) {
        AddEntryDialog(
            param = selectedParam!!,
            onDismiss = { showAddEntryDialog = false },
            onSave = { entry ->
                entriesViewModel.createMonitoringEntry(
                    entry,
                    selectedChild!!.id,
                    selectedParam!!.id
                )
            }
        )
    }
}
