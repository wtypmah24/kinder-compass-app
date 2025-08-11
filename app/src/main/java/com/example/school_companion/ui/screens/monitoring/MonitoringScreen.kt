package com.example.school_companion.ui.screens.monitoring

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.wear.compose.material.ContentAlpha
import com.example.school_companion.data.model.Child
import com.example.school_companion.data.model.MonitoringEntry
import com.example.school_companion.data.model.MonitoringParam
import com.example.school_companion.ui.dialog.entry.AddEntryDialog
import com.example.school_companion.ui.dialog.param.AddParamDialog
import com.example.school_companion.ui.dialog.entry.EditEntryDialog
import com.example.school_companion.ui.dialog.param.EditParamDialog
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
fun MonitoringScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    paramsViewModel: MonitoringParamViewModel = hiltViewModel(),
    entriesViewModel: MonitoringEntryViewModel = hiltViewModel(),
    childrenViewModel: ChildrenViewModel = hiltViewModel()
) {
    val authToken by authViewModel.authToken.collectAsStateWithLifecycle()
    val paramsState by paramsViewModel.paramsState.collectAsStateWithLifecycle()
    val entriesState by entriesViewModel.entriesState.collectAsStateWithLifecycle()
    val childrenState by childrenViewModel.childrenState.collectAsStateWithLifecycle()

    var selectedChild: Child? by remember { mutableStateOf(null) }
    var selectedParam: MonitoringParam? by remember { mutableStateOf(null) }
    var showAddEntryDialog by remember { mutableStateOf(false) }
    var showAddParamDialog by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
//    val context = LocalContext.current

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
                title = { Text("Monitoring", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showAddParamDialog = true },
                        enabled = selectedChild != null && selectedParam != null
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Monitoring Entry")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {

                if ((childrenState is ChildrenState.Success) && (paramsState is ParamsState.Success)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Select Child", fontWeight = FontWeight.Medium)
                        DropdownMenuWrapper(
                            items = (childrenState as ChildrenState.Success).children,
                            selectedItem = selectedChild,
                            onItemSelected = { selectedChild = it },
                            itemToString = { "${it.name} ${it.surname}" },
                            placeholder = "Choose child"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Select Monitoring Parameter", fontWeight = FontWeight.Medium)
                        DropdownMenuWrapper(
                            items = (paramsState as ParamsState.Success).paramData,
                            selectedItem = selectedParam,
                            onItemSelected = { selectedParam = it },
                            itemToString = { it.title },
                            placeholder = "Choose parameter"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val tabs = listOf("Monitoring Parameters", "Last Monitoring Entries")

            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTabIndex) {

                0 -> { // Monitoring Parameters
                    if (paramsState is ParamsState.Success) {
                        val params = (paramsState as ParamsState.Success).paramData
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = { showAddParamDialog = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Add New Parameter")
                            }
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(params) { param ->
                                    authToken?.let {
                                        MonitoringParamCard(
                                            param, paramsViewModel,
                                            it
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> { // Last Monitoring Entries
                    if (entriesState is EntriesState.Success && childrenState is ChildrenState.Success) {
                        val entries = (entriesState as EntriesState.Success).entryData
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = { showAddEntryDialog = true },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = selectedChild != null && selectedParam != null
                            ) {
                                Text("Add New Entry")
                            }
                            //TODO: FIX replace unnecessary 'find'
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(entries) { entry ->
                                    val child: Child? =
                                        (childrenState as ChildrenState.Success).children.find { c -> entry.childId == c.id }
                                    if (child != null) {
                                        authToken?.let {
                                            MonitoringEntryCard(
                                                entry,
                                                child,
                                                entriesViewModel,
                                                it
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        if (showAddParamDialog && authToken != null) {
            AddParamDialog(
                onDismiss = { showAddParamDialog = false },
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownMenuWrapper(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemToString: (T) -> String,
    placeholder: String
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = selectedItem?.let { itemToString(it) } ?: placeholder

    Box {
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
                disabledBorderColor = MaterialTheme.colorScheme.primary,
                disabledTrailingIconColor = MaterialTheme.colorScheme.primary,
                disabledLabelColor = MaterialTheme.colorScheme.primary,
            ),
            enabled = false
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(itemToString(item)) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonitoringParamCard(
    param: MonitoringParam,
    paramsViewModel: MonitoringParamViewModel,
    token: String
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(param.title, fontWeight = FontWeight.Bold)
                Text("Type: ${param.type}")
                Text(param.description)
                Text("Min: ${param.minValue}, Max: ${param.maxValue}")
                Text("Created at: ${param.createdAt}")
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            IconButton(onClick = { showEditDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Monitoring Parameter",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Monitoring Parameter",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        if (showEditDialog) {
            EditParamDialog(
                param = param,
                onDismiss = { showEditDialog = false },
                onSave = { updateParamRequestDto ->
                    paramsViewModel.updateMonitoringParam(
                        token = token,
                        param = updateParamRequestDto,
                        paramId = param.id
                    )
                    showEditDialog = false
                }
            )
        }

        if (showDeleteConfirm) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                title = { Text("Delete Monitoring Parameter?") },
                text = { Text("Are you sure you want to delete «${param.title}: ${param.description}»?") },
                confirmButton = {
                    Button(
                        onClick = {
                            paramsViewModel.deleteMonitoringParam(token, param.id)
                            showDeleteConfirm = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteConfirm = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonitoringEntryCard(
    entry: MonitoringEntry,
    child: Child,
    entriesViewModel: MonitoringEntryViewModel,
    token: String
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(entry.parameterName, fontWeight = FontWeight.Bold)
                Text("Value: ${entry.value}")
                Text("Child: ${child.name} ${child.surname}")
                Text("Notes: ${entry.notes}")
                Text("Type: ${entry.type}")
                Text("Created at: ${entry.createdAt}")
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            IconButton(onClick = { showEditDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Monitoring Entry",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Monitoring Entry",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        if (showEditDialog) {
            EditEntryDialog(
                entry = entry,
                onDismiss = { showEditDialog = false },
                onSave = { updateEntryRequestDto ->
                    entriesViewModel.updateMonitoringEntry(
                        token = token,
                        entry = updateEntryRequestDto,
                        childId = child.id,
                        paramId = entry.parameterId
                    )
                    showEditDialog = false
                }
            )
        }

        if (showDeleteConfirm) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                title = { Text("Delete Monitoring Entry?") },
                text = { Text("Are you sure you want to delete «${entry.type}: ${entry.value}»?") },
                confirmButton = {
                    Button(
                        onClick = {
                            entriesViewModel.deleteMonitoringEntry(token, entry.id, child.id)
                            showDeleteConfirm = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteConfirm = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
