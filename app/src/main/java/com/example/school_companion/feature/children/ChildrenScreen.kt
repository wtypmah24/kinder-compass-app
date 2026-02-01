package com.example.school_companion.feature.children

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.ui.util.ChildActionHandler

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildrenScreen(
    onNavigate: NavigateToWithArgs,
    childrenViewModel: ChildrenViewModel
) {
    val childrenState by childrenViewModel.childrenState.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }

    ChildrenContent(
        childrenState = childrenState,
        showAddDialog = showAddDialog,
        onAddClick = { showAddDialog = true },
        onDismissAddDialog = { showAddDialog = false },
        onSaveChild = { dto -> childrenViewModel.addChild(dto) },
        onChildAction = { child, action ->
            ChildActionHandler.handle(
                child,
                action,
                onNavigate,
                onDeleteChild = { id -> childrenViewModel.deleteChild(id) },
                onEditChild = { id, updatedChild ->
                    childrenViewModel.updateChild(
                        id,
                        updatedChild
                    )
                },
                onSetSelectedChild = { selectedChild ->
                    childrenViewModel.setSelectedChild(
                        selectedChild
                    )
                }
            )
        },
        onNavigateBack = { onNavigate(null, null) }
    )
}