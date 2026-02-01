package com.example.school_companion.feature.children

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.school_companion.data.api.ChildDto
import com.example.school_companion.data.model.Child
import com.example.school_companion.feature.children.child.ChildAction
import com.example.school_companion.ui.util.UiState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildrenContent(
    childrenState: UiState<List<Child>>,
    showAddDialog: Boolean,
    onAddClick: () -> Unit,
    onDismissAddDialog: () -> Unit,
    onSaveChild: (ChildDto) -> Unit,
    onChildAction: (Child, ChildAction) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kinder", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onAddClick) {
                        Icon(Icons.Default.Add, contentDescription = "Add Child")
                    }
                }
            )
        }
    ) { paddingValues ->
        ChildrenSection(
            childrenState = childrenState,
            onChildAction = onChildAction,
            maxItems = Int.MAX_VALUE,
            onShowAllClick = null,
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }

    if (showAddDialog) {
        AddChildDialog(
            onDismiss = onDismissAddDialog,
            onSave = onSaveChild
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Children Content - Normal")
@Composable
fun ChildrenContentPreview_Normal() {
    val sampleChildren = listOf(
        Child(
            id = 1L,
            name = "Alice",
            surname = "Johnson",
            email = "alice@example.com",
            phoneNumber = "+123456789",
            dateOfBirth = "2015-06-21",
            active = true
        ),
        Child(
            id = 2L,
            name = "Bob",
            surname = "Smith",
            email = "bob@example.com",
            phoneNumber = "+987654321",
            dateOfBirth = "2013-09-12",
            active = true
        )
    )

    ChildrenContent(
        childrenState = UiState.Success(sampleChildren),
        showAddDialog = false,
        onAddClick = {},
        onDismissAddDialog = {},
        onSaveChild = {},
        onChildAction = { _, _ -> },
        onNavigateBack = {}
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Children Content - Add Dialog")
@Composable
fun ChildrenContentPreview_AddDialog() {
    val sampleChildren = listOf(
        Child(
            id = 1L,
            name = "Alice",
            surname = "Johnson",
            email = "alice@example.com",
            phoneNumber = "+123456789",
            dateOfBirth = "2015-06-21",
            active = true
        )
    )

    ChildrenContent(
        childrenState = UiState.Success(sampleChildren),
        showAddDialog = true,
        onAddClick = {},
        onDismissAddDialog = {},
        onSaveChild = {},
        onChildAction = { _, _ -> },
        onNavigateBack = {}
    )
}
