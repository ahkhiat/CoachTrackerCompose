package com.devid_academy.coachtrackercompose.ui.screen.main

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devid_academy.coachtrackercompose.data.dto.EventDTO
import com.devid_academy.coachtrackercompose.ui.navigation.Screen
import com.devid_academy.coachtrackercompose.ui.screen.auth.AuthViewModel
import com.devid_academy.coachtrackercompose.ui.screen.profile.ProfileViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    profileViewModel: ProfileViewModel
) {

    val eventList by mainViewModel.events.collectAsState()
    val direction by authViewModel.directionStateFlow.collectAsState()
    val teamName by profileViewModel.teamNameStateFlow.collectAsState()


    LaunchedEffect(direction) {
        direction?.let {
            navController.navigate(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = teamName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onPrimary)
                            .padding(8.dp)
                            .clickable {
                                navController.navigate(Screen.Profile.route)
                            }

                    ) {
                        Text(
                            text = "AB",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar {
                Text("Barre du bas", modifier = Modifier.padding(16.dp))
            }
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
                EventContent(eventList = eventList,
//                    modifier = Modifier.weight(1f)
                )
            }
        }
    )
}



