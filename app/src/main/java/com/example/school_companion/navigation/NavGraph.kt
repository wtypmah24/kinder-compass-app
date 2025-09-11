package com.example.school_companion.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.school_companion.feature.assistant.AssistantScreen
import com.example.school_companion.feature.children.child.ChildDetailScreen
import com.example.school_companion.feature.children.ChildrenScreen
import com.example.school_companion.feature.auth.login.LoginScreen
import com.example.school_companion.feature.auth.register.RegisterScreen
import com.example.school_companion.feature.dashboard.DashboardScreen
import com.example.school_companion.feature.event.EventsScreen
import com.example.school_companion.feature.monitoring.MonitoringScreen
import com.example.school_companion.feature.profile.ProfileScreen
import com.example.school_companion.feature.settings.SettingsScreen
import com.example.school_companion.feature.statistic.StatisticsScreen
import com.example.school_companion.feature.auth.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // Auth screens
        composable(Screen.Login.route) {
            LoginScreen(navController = navController, authViewModel)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController = navController, authViewModel)
        }

        // Main screens
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(Screen.Children.route) {
            ChildrenScreen(navController = navController)
        }

        composable(
            route = Screen.ChildDetail.route + "/{childId}",
            arguments = listOf(
                navArgument("childId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val childId = backStackEntry.arguments?.getLong("childId")
            ChildDetailScreen(
                navController = navController,
                childId = childId ?: 0L,
            )
        }

        composable(Screen.Events.route) {
            EventsScreen(navController = navController)
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen(navController = navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }

        composable(Screen.Monitoring.route) {
            MonitoringScreen(navController = navController)
        }

        composable(Screen.Assistant.route) {
            AssistantScreen(navController = navController)
        }
    }
} 