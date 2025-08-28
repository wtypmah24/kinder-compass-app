package com.example.school_companion.ui.screens.profile

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.school_companion.ui.bar.dashboard.DashBoardBottomBar
import com.example.school_companion.ui.bar.profile.ProfileTopBar
import com.example.school_companion.ui.card.profile.UserInfoCard
import com.example.school_companion.ui.card.profile.WorkSessionCard
import com.example.school_companion.ui.card.profile.WorkSessionReportCard
import com.example.school_companion.ui.viewmodel.AuthViewModel
import com.example.school_companion.ui.viewmodel.CompanionViewModel
import com.example.school_companion.ui.viewmodel.WorkSessionViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    workSessionViewModel: WorkSessionViewModel = hiltViewModel(),
    companionViewModel: CompanionViewModel = hiltViewModel()

) {
    val currentUser by authViewModel.currentCompanion.collectAsStateWithLifecycle()
    val currentSession by workSessionViewModel.session.collectAsStateWithLifecycle()
    var selectedBottomTabIndex by remember { mutableIntStateOf(0) }

    val context = LocalContext.current

    val pickImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri: Uri? ->
                uri?.let {
                    Toast.makeText(context, "Chosen: $uri", Toast.LENGTH_SHORT).show()
                }
            })

    LaunchedEffect(Unit) {
        workSessionViewModel.status()
    }

    Scaffold(topBar = {
        ProfileTopBar(navController)
    }, bottomBar = {
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
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                UserInfoCard(
                    currentUser = currentUser,
                    pickImageLauncher = pickImageLauncher,
                    companionViewModel = companionViewModel,
                    authViewModel
                )
            }
            item {
                WorkSessionCard(
                    currentSession = currentSession,
                    workSessionViewModel = workSessionViewModel
                )
            }
            item {
                WorkSessionReportCard(workSessionViewModel)
            }
        }
    }
}
