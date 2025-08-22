package com.example.school_companion.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Dashboard : Screen("dashboard")
    data object Children : Screen("children")
    data object ChildDetail : Screen("child_detail")
    data object Events : Screen("events")
    data object Monitoring : Screen("monitoring")
    data object Statistics : Screen("statistics")
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
    data object Assistant : Screen("assistant")
}