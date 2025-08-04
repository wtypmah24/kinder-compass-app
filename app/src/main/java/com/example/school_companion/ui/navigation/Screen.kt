package com.example.school_companion.ui.navigation

sealed class Screen(val route: String) {
    // Auth screens
    object Login : Screen("login")
    object Register : Screen("register")
    
    // Main screens
    object Dashboard : Screen("dashboard")
    object Children : Screen("children")
    object ChildDetail : Screen("child_detail")
    object Events : Screen("events")
    object Monitoring : Screen("monitoring")
    object Notes : Screen("notes")
    object Statistics : Screen("statistics")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
} 