package com.devid_academy.coachtrackercompose.ui.navigation

import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavController) {
    BottomAppBar(
        backgroundColor = Color.White,
        contentColor = Color.Gray

    ) {
        val items = listOf(
            BottomNavItem("home", Icons.Default.CalendarToday, Screen.Main.route),
            BottomNavItem("team", Icons.Default.Groups, Screen.Team.route),
            BottomNavItem("profile", Icons.Default.PersonPin, Screen.Profile.route)
        )

        BottomNavigation(
            backgroundColor = Color.White
        ) {
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

            items.forEach { item ->
                BottomNavigationItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = currentRoute == item.route,
                    onClick = {
//                        navController.navigate(item.route) {
//                            popUpTo(navController.graph.startDestinationId) { saveState = true }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
                        navController.navigate(item.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

}

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)
