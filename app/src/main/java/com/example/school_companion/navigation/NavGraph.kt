package com.example.school_companion.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.school_companion.feature.assistant.AssistantScreen
import com.example.school_companion.feature.auth.AuthViewModel
import com.example.school_companion.feature.auth.login.LoginScreen
import com.example.school_companion.feature.auth.register.RegisterScreen
import com.example.school_companion.feature.children.ChildrenScreen
import com.example.school_companion.feature.children.ChildrenViewModel
import com.example.school_companion.feature.children.child.ChildDetailScreen
import com.example.school_companion.feature.dashboard.DashboardScreen
import com.example.school_companion.feature.dashboard.QuickActionsData
import com.example.school_companion.feature.event.EventsScreen
import com.example.school_companion.feature.event.EventsViewModel
import com.example.school_companion.feature.monitoring.MonitoringScreen
import com.example.school_companion.feature.profile.ProfileScreen
import com.example.school_companion.feature.settings.SettingsScreen
import com.example.school_companion.feature.statistic.StatisticsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route,
    authViewModel: AuthViewModel = hiltViewModel(),
    childrenViewModel: ChildrenViewModel = hiltViewModel(),
    eventsViewModel: EventsViewModel = hiltViewModel(),
) {
    val childrenState by childrenViewModel.childrenState.collectAsStateWithLifecycle()
    val companionState by authViewModel.currentCompanion.collectAsStateWithLifecycle()
    val eventsState by eventsViewModel.eventsState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        childrenViewModel.loadChildren()
        authViewModel.getUserProfile()
        eventsViewModel.loadEventsByCompanion()
    }

    val navigateTo: NavigateToWithArgs = { screen, args ->
        if (screen == null) {
            navController.navigateUp()
        } else {
            val route = if (args != null && args.isNotEmpty()) {
                var r = screen.route
                args.forEach { (_, value) ->
                    r += "/$value"
                }
                r
            } else {
                screen.route
            }
            navController.navigate(route)
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // Auth screens
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigate = navigateTo,
                viewModel = authViewModel
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigate = navigateTo,
                viewModel = authViewModel
            )
        }

        // Main screens
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigate = navigateTo,
                currentUserState = companionState,
                childrenState = childrenState,
                eventsState = eventsState,
                quickActions = QuickActionsData.getQuickActions(navigateTo)
            )
        }

        composable(Screen.Children.route) {
            ChildrenScreen(onNavigate = navigateTo, childrenState = childrenState)
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
