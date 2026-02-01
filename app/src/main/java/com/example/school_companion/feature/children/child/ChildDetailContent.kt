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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.school_companion.data.model.Child
import com.example.school_companion.navigation.NavigateToWithArgs
import com.example.school_companion.ui.box.ErrorBox
import com.example.school_companion.ui.box.LoadingBox
import com.example.school_companion.ui.util.UiState
import com.example.school_companion.ui.util.onState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildDetailContent(
    selectedChildState: UiState<Child>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigate: NavigateToWithArgs,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    selectedChildState.onState(
                        onLoading = { LoadingBox() },
                        onError = { msg -> ErrorBox(message = msg) },
                        onSuccess = { child ->
                            Text(
                                text = "${child.name} ${child.surname}",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues)
        ) {
            selectedChildState.onState(
                onLoading = { LoadingBox() },
                onError = { msg -> ErrorBox(message = msg) },
                onSuccess = { child ->
                    ChildHeaderCard(child = child)
                    ChildTabs(
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = onTabSelected
                    )
                    ChildTabContent(
                        index = selectedTabIndex,
                        child = child,
                        onNavigate = onNavigate
                    )
                }
            )
        }
    }
}
