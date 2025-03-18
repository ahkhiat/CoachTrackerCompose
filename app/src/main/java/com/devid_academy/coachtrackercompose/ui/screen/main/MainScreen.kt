package com.devid_academy.coachtrackercompose.ui.screen.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devid_academy.coachtrackercompose.data.dto.EventDTO
import com.devid_academy.coachtrackercompose.ui.navigation.BottomBar
import com.devid_academy.coachtrackercompose.ui.navigation.Screen
import com.devid_academy.coachtrackercompose.ui.screen.auth.AuthViewModel
import com.devid_academy.coachtrackercompose.ui.screen.profile.ProfileViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel
) {

    val eventList by mainViewModel.eventsStateFlow.collectAsState()
//    val direction by authViewModel.directionStateFlow.collectAsState()
    val teamNameStateFlow by mainViewModel.teamNameStateFlow.collectAsState()


//    LaunchedEffect(direction) {
//        direction?.let {
//            navController.navigate(it)
//        }
//    }

    LaunchedEffect(true) {
        mainViewModel.mainSharedFlow.collect { direction ->
            direction?.let {
                when {
                    it == Screen.Login.route -> {
                        navController.navigate(it) {
                            popUpTo(Screen.Main.route) {
                                inclusive = true
                            }
                        }
                    }
                    it.startsWith("details/") -> {
                        navController.navigate(it)
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = teamNameStateFlow,
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
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.CreateEvent.route)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter")
            }
        },
        content = { paddingValues ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                EventContent(
                    eventList = eventList,
                    onClick = {
                        Log.i("MAIN SCREEN", "Main Nav Event Click : $it")
                        mainViewModel.navigateToDetails(it.id)
                    }
                )
            }
        }
    )
}




