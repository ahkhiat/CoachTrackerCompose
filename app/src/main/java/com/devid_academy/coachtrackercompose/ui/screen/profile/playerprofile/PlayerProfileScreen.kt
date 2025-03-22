package com.devid_academy.coachtrackercompose.ui.screen.profile.playerprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devid_academy.coachtrackercompose.ui.navigation.BottomBar
import com.devid_academy.coachtrackercompose.ui.navigation.Screen
import com.devid_academy.coachtrackercompose.ui.screen.profile.privateprofile.ProfileScreenContent

@Composable
fun PlayerProfileScreen(
    navController: NavController,
    playerProfileViewModel: PlayerProfileViewModel
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {},
                colors = TopAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    scrolledContainerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomBar(navController = navController)
        },
        content = { paddingValues ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                ProfileScreenContent(
                    firstname = userStateFlow.firstname,
                    lastname = userStateFlow.lastname,
                    email = userStateFlow.email,
                    birthdate = userStateFlow.birthdate,
                    phone = userStateFlow.phone!!,
                    team = teamName,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route)
                    },
                    onExit = {
                        navController.popBackStack()
                    }
                )
            }
        }
    )
}

@Composable
fun PlayerProfileContent() {

}

@Preview(showBackground = true)
@Composable
fun PlayerProfilePreview() {
    PlayerProfileContent()
}

