package com.example.school_companion.ui.screens.children

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.school_companion.ui.bar.dashboard.DashBoardBottomBar
import com.example.school_companion.ui.section.children.ChildrenSection
import com.example.school_companion.ui.viewmodel.AuthViewModel
import com.example.school_companion.ui.viewmodel.ChildrenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildrenScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    childrenViewModel: ChildrenViewModel = hiltViewModel()
) {
    val authToken by authViewModel.authToken.collectAsStateWithLifecycle()
    val childrenState by childrenViewModel.childrenState.collectAsStateWithLifecycle()
    var selectedBottomTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(authToken) {
        authToken?.let { token ->
            childrenViewModel.loadChildren(token)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kinder",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            DashBoardBottomBar(
                navController = navController,
                selectedTabIndex = selectedBottomTabIndex,
                onTabSelected = { selectedBottomTabIndex = it }
            )
        }
    ) { paddingValues ->
        ChildrenSection(
            childrenState = childrenState,
            childrenViewModel = childrenViewModel,
            navController = navController,
            maxItems = Int.MAX_VALUE,
            onShowAllClick = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
