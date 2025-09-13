package com.example.school_companion.feature.children.child

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.school_companion.data.model.Child
import com.example.school_companion.feature.children.ChildrenViewModel
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.util.UiState
import com.example.school_companion.ui.util.onState

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


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    selectedChild.onState(
                        onLoading = { LoadingBox() },
                        onError = { msg -> ErrorBox(message = msg) },
                        onSuccess = {
                            Text(
                                text = "${it.name} ${it.surname}",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(null, null) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            selectedChild.onState(
                onLoading = { LoadingBox() },
                onError = { msg -> ErrorBox(message = msg) },
                onSuccess = {
                    ChildHeaderCard(child = it)
                    ChildTabs(
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { selectedTabIndex = it }
                    )
                    ChildTabContent(
                        index = selectedTabIndex,
                        child = it,
                        onNavigate = onNavigate,
                    )
                }
            )
        }
    }
}