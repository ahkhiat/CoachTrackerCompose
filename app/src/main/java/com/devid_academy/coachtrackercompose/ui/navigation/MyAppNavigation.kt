package com.devid_academy.coachtrackercompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devid_academy.coachtrackercompose.ui.screen.auth.LoginScreen
import com.devid_academy.coachtrackercompose.ui.screen.SplashScreen
import com.devid_academy.coachtrackercompose.ui.screen.main.MainScreen

@Composable
fun MyAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("main") { MainScreen(navController) }
    }
}
