package com.example.school_companion.feature.children.child

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.data.model.Child
import com.example.school_companion.feature.children.ChildrenViewModel
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.ui.util.UiState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildDetailScreen(
    onNavigate: NavigateToWithArgs,
    childId: Long,
    childrenViewModel: ChildrenViewModel,
) {
    val selectedChild by childrenViewModel.selectedChild.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(childId) {
        if (selectedChild !is UiState.Success || (selectedChild as UiState.Success<Child>).data.id != childId) {
            childrenViewModel.loadChild(childId)
        }
    }

    ChildDetailContent(
        selectedChildState = selectedChild,
        selectedTabIndex = selectedTabIndex,
        onTabSelected = { selectedTabIndex = it },
        onNavigateBack = { onNavigate(null, null) },
        onNavigate = onNavigate,
        modifier = Modifier.fillMaxSize()
    )
}