package com.devid_academy.coachtrackercompose.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devid_academy.coachtrackercompose.ui.screen.auth.AuthViewModel
import com.devid_academy.coachtrackercompose.ui.screen.auth.LoginScreen
import com.devid_academy.coachtrackercompose.ui.screen.auth.LoginViewModel
import com.devid_academy.coachtrackercompose.ui.screen.auth.RegisterScreen
import com.devid_academy.coachtrackercompose.ui.screen.create.CreateEventScreen
import com.devid_academy.coachtrackercompose.ui.screen.create.CreateEventViewModel
import com.devid_academy.coachtrackercompose.ui.screen.details.DetailsScreen
import com.devid_academy.coachtrackercompose.ui.screen.details.DetailsViewModel
import com.devid_academy.coachtrackercompose.ui.screen.splash.SplashScreen
import com.devid_academy.coachtrackercompose.ui.screen.main.MainScreen
import com.devid_academy.coachtrackercompose.ui.screen.main.MainViewModel
import com.devid_academy.coachtrackercompose.ui.screen.profile.ProfileScreen
import com.devid_academy.coachtrackercompose.ui.screen.profile.ProfileViewModel
import com.devid_academy.coachtrackercompose.ui.screen.splash.SplashViewModel
import com.devid_academy.coachtrackercompose.ui.screen.team.TeamScreen
import com.devid_academy.coachtrackercompose.ui.screen.team.TeamViewModel

@Composable
fun MyAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController, splashViewModel)
        }
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController, loginViewModel)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screen.Main.route) {
            val mainViewModel: MainViewModel = hiltViewModel()
            val authViewModel: AuthViewModel = hiltViewModel()
            MainScreen(navController, authViewModel,mainViewModel)
        }
        composable(
            Screen.Profile.route,
//            enterTransition = {
//                slideInVertically { -it } },
//            exitTransition = { slideOutVertically { -it } }
            ) {
                val authViewModel: AuthViewModel = hiltViewModel()
                val profileViewModel: ProfileViewModel = hiltViewModel()
                ProfileScreen(navController, authViewModel, profileViewModel)
        }
        composable(Screen.CreateEvent.route) {
            val createEventViewModel: CreateEventViewModel = hiltViewModel()
            CreateEventScreen(navController, createEventViewModel)
        }
        composable(Screen.Team.route) {
            val teamViewModel: TeamViewModel = hiltViewModel()
            TeamScreen(navController, teamViewModel)
        }
        composable(
            route = Screen.Details.route + "/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val eventId = navBackStackEntry.arguments?.getInt("eventId") ?: 0
            val detailsViewModel: DetailsViewModel = hiltViewModel()
            DetailsScreen(navController, detailsViewModel, eventId)
        }
    }
}



sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main : Screen("main")
    object Login : Screen("login")
    object Register : Screen("register")
    object Profile: Screen("profile")
    object CreateEvent: Screen("create_event")
    object Team: Screen("team")
    object Details: Screen("details")
}



