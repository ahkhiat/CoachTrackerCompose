package com.devid_academy.coachtrackercompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devid_academy.coachtrackercompose.ui.screen.auth.AuthViewModel
import com.devid_academy.coachtrackercompose.ui.screen.auth.LoginScreen
import com.devid_academy.coachtrackercompose.ui.screen.auth.LoginViewModel
import com.devid_academy.coachtrackercompose.ui.screen.auth.RegisterScreen
import com.devid_academy.coachtrackercompose.ui.screen.splash.SplashScreen
import com.devid_academy.coachtrackercompose.ui.screen.main.MainScreen
import com.devid_academy.coachtrackercompose.ui.screen.main.MainViewModel
import com.devid_academy.coachtrackercompose.ui.screen.splash.SplashViewModel

@Composable
fun MyAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SPLASH) {
        composable(SPLASH) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController, splashViewModel)
        }
        composable(LOGIN) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController, loginViewModel)
        }
        composable(REGISTER) {
            RegisterScreen(navController)
        }
        composable(MAIN) {
            val mainViewModel: MainViewModel = hiltViewModel()
            val authViewModel: AuthViewModel = hiltViewModel()
            MainScreen(navController, authViewModel,mainViewModel)
        }
    }
}

const val SPLASH = "splash"
const val LOGIN = "login"
const val REGISTER = "register"
const val MAIN = "main"


