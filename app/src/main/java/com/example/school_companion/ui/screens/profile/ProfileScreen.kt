package com.example.school_companion.ui.screens.profile

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.school_companion.data.model.WorkSession
import com.example.school_companion.ui.card.profile.AvatarCard
import com.example.school_companion.ui.card.profile.UserInfoCard
import com.example.school_companion.ui.card.profile.WorkSessionCard
import com.example.school_companion.ui.card.profile.WorkSessionReportCard
import com.example.school_companion.ui.dialog.session.EditSessionDialog
import com.example.school_companion.ui.navigation.Screen
import com.example.school_companion.ui.viewmodel.AuthViewModel
import com.example.school_companion.ui.viewmodel.CompanionViewModel
import com.example.school_companion.ui.viewmodel.SessionState
import com.example.school_companion.ui.viewmodel.SessionsState
import com.example.school_companion.ui.viewmodel.WorkSessionViewModel
import java.time.LocalDate
import java.util.Calendar


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    workSessionViewModel: WorkSessionViewModel = hiltViewModel(),
    companionViewModel: CompanionViewModel = hiltViewModel()

) {
    val currentUser by authViewModel.currentCompanion.collectAsStateWithLifecycle()
    val currentSession by workSessionViewModel.session.collectAsStateWithLifecycle()
    val authToken by authViewModel.authToken.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                Toast.makeText(context, "Chosen: $uri", Toast.LENGTH_SHORT).show()
            }
        })

    LaunchedEffect(authToken) {
        authToken?.let { token ->
            workSessionViewModel.status(token)
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "Profile", fontWeight = FontWeight.Bold
            )
        }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }, actions = {
            IconButton(onClick = { navController.navigate(Screen.Login.route) }) {
                Icon(Icons.Default.Logout, contentDescription = "Logout")
            }
        })
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                authToken?.let {
                    UserInfoCard(
                        currentUser = currentUser,
                        pickImageLauncher = pickImageLauncher,
                        companionViewModel = companionViewModel,
                        token = it,
                        authViewModel
                    )
                }
            }
            item {
                WorkSessionCard(
                    currentSession = currentSession,
                    authToken = authToken,
                    workSessionViewModel = workSessionViewModel
                )
            }
            item {
                WorkSessionReportCard(authToken, workSessionViewModel)
            }
        }
    }
}

