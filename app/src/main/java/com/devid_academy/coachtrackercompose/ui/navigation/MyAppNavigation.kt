package com.devid_academy.coachtrackercompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devid_academy.coachtrackercompose.ui.screen.auth.AuthViewModel
import com.devid_academy.coachtrackercompose.ui.screen.auth.LoginScreen
import com.devid_academy.coachtrackercompose.ui.screen.auth.LoginViewModel
import com.devid_academy.coachtrackercompose.ui.screen.auth.RegisterScreen
import com.devid_academy.coachtrackercompose.ui.screen.auth.RegisterViewModel
import com.devid_academy.coachtrackercompose.ui.screen.convocation.createconvocation.CreateConvocationScreen
import com.devid_academy.coachtrackercompose.ui.screen.convocation.createconvocation.CreateConvocationViewModel
import com.devid_academy.coachtrackercompose.ui.screen.event.createevent.CreateEventScreen
import com.devid_academy.coachtrackercompose.ui.screen.event.createevent.CreateEventViewModel
import com.devid_academy.coachtrackercompose.ui.screen.details.DetailsScreen
import com.devid_academy.coachtrackercompose.ui.screen.details.DetailsViewModel
import com.devid_academy.coachtrackercompose.ui.screen.convocation.editconvocation.EditConvocationScreen
import com.devid_academy.coachtrackercompose.ui.screen.convocation.editconvocation.EditConvocationViewModel
import com.devid_academy.coachtrackercompose.ui.screen.splash.SplashScreen
import com.devid_academy.coachtrackercompose.ui.screen.main.MainScreen
import com.devid_academy.coachtrackercompose.ui.screen.main.MainViewModel
import com.devid_academy.coachtrackercompose.ui.screen.notification.NotificationScreen
import com.devid_academy.coachtrackercompose.ui.screen.notification.NotificationViewModel
import com.devid_academy.coachtrackercompose.ui.screen.presence.createpresence.CreatePresenceScreen
import com.devid_academy.coachtrackercompose.ui.screen.presence.createpresence.CreatePresenceViewModel
import com.devid_academy.coachtrackercompose.ui.screen.profile.playerprofile.PlayerProfileScreen
import com.devid_academy.coachtrackercompose.ui.screen.profile.playerprofile.PlayerProfileViewModel
import com.devid_academy.coachtrackercompose.ui.screen.profile.privateprofile.PrivateProfileScreen
import com.devid_academy.coachtrackercompose.ui.screen.profile.privateprofile.PrivateProfileViewModel
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
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(navController, registerViewModel)
        }
        composable(Screen.Main.route) {
            val mainViewModel: MainViewModel = hiltViewModel()
            val authViewModel: AuthViewModel = hiltViewModel()
            MainScreen(navController, authViewModel,mainViewModel)
        }
        composable(Screen.PrivateProfile.route) {
                val authViewModel: AuthViewModel = hiltViewModel()
                val privateProfileViewModel: PrivateProfileViewModel = hiltViewModel()
                PrivateProfileScreen(navController, authViewModel, privateProfileViewModel)
        }
        composable(Screen.PlayerProfile.route) {
            val playerProfileViewModel: PlayerProfileViewModel = hiltViewModel()
            PlayerProfileScreen(navController, playerProfileViewModel)
        }
        composable(Screen.CreateEvent.route) {
            val createEventViewModel: CreateEventViewModel = hiltViewModel()
            CreateEventScreen(navController, createEventViewModel)
        }
        composable(Screen.Team.route) {
            val teamViewModel: TeamViewModel = hiltViewModel()
            TeamScreen(navController, teamViewModel)
        }
        composable(Screen.Notification.route) {
            val notificationViewModel: NotificationViewModel = hiltViewModel()
            NotificationScreen(navController, notificationViewModel)
        }
        composable(
            route = Screen.Details.route + "/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) {
            val eventId = it.arguments?.getInt("eventId") ?: 0
            val detailsViewModel: DetailsViewModel = hiltViewModel()
            DetailsScreen(navController, detailsViewModel, eventId)
        }
        composable(
            route = Screen.CreateConvocation.route + "/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) {
            val eventId = it.arguments?.getInt("eventId") ?: 0
            val createConvocationViewModel: CreateConvocationViewModel = hiltViewModel()
            CreateConvocationScreen(navController, createConvocationViewModel, eventId)
        }
        composable(
            route = Screen.EditConvocation.route + "/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) {
            val eventId = it.arguments?.getInt("eventId") ?: 0
            val editConvocationViewModel: EditConvocationViewModel = hiltViewModel()
            EditConvocationScreen(navController, editConvocationViewModel, eventId)
        }
        composable(
            route = Screen.CreatePresence.route + "/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) {
            val eventId = it.arguments?.getInt("eventId") ?: 0
            val createPresenceViewModel: CreatePresenceViewModel = hiltViewModel()
            CreatePresenceScreen(navController, createPresenceViewModel, eventId)
        }
    }
}



sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main : Screen("main")
    object Login : Screen("login")
    object Register : Screen("register")
    object PrivateProfile: Screen("private_profile")
    object CreateEvent: Screen("create_event")
    object Team: Screen("team")
    object Details: Screen("details")
    object CreateConvocation: Screen("create_convocation")
    object EditConvocation: Screen("edit_convocation")
    object Notification: Screen("notification")
    object CreatePresence: Screen("create_presence")
}



