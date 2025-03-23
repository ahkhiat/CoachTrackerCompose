package com.devid_academy.coachtrackercompose.ui.screen.profile.playerprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devid_academy.coachtrackercompose.ui.navigation.BottomBar
import com.devid_academy.coachtrackercompose.ui.navigation.Screen
import com.devid_academy.coachtrackercompose.ui.screen.profile.privateprofile.ProfileLine
import com.devid_academy.coachtrackercompose.ui.screen.profile.privateprofile.ProfileScreenContent
import com.devid_academy.coachtrackercompose.util.ViewModelEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerProfileScreen(
    navController: NavController,
    playerProfileViewModel: PlayerProfileViewModel,
    userId: Int
){
    val playerProfileStateFlow by playerProfileViewModel
        .playerStateFlow.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(userId) {
        playerProfileViewModel.getPlayerProfile(userId)
    }

    LaunchedEffect(true) {
        playerProfileViewModel.playerProfileSharedFlow.collect { event ->
            when (event) {
                is ViewModelEvent.NavigateToMainScreen -> {
                    navController.popBackStack()
                }
                is ViewModelEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(context.getString(event.resId))
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Player profile",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
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
                PlayerProfileContent(
                    firstname = playerProfileStateFlow.firstname,
                    lastname = playerProfileStateFlow.lastname,
                    email = playerProfileStateFlow.email,
                    birthdate = playerProfileStateFlow.birthdate,
                    phone = playerProfileStateFlow.phone ?: "Not provided",
                    team = playerProfileStateFlow.playsIn?.name ?: "",
                    convocationsCount = playerProfileStateFlow.stats?.convocations ?: 0,
                    presencesCount = playerProfileStateFlow.stats?.presences ?: 0,
                    goalsCount = playerProfileStateFlow.stats?.goals ?: 0
                )
            }
        }
    )
}

@Composable
fun PlayerProfileContent(
    firstname: String,
    lastname: String,
    email: String,
    birthdate: String,
    phone: String,
    team: String,
    convocationsCount: Int,
    presencesCount: Int,
    goalsCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "$firstname $lastname",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold)
            }
        }
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row() {
                    Text(
                        text = "Informations",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                ProfileLine(title = "Email", content = email)
                Spacer(modifier = Modifier.height(8.dp))
                ProfileLine(title = "Birthdate", content = birthdate)
                Spacer(modifier = Modifier.height(8.dp))
                ProfileLine(title = "Phone", content = phone)
            }
        }
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Team",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = team,
                    fontSize = 16.sp
                )
            }
        }
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Statistiques",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                ProfileLine(title = "Matchs convoqués", content = convocationsCount.toString() )
                Spacer(modifier = Modifier.height(8.dp))
                ProfileLine(title = "Matchs joués", content = presencesCount.toString() )
                Spacer(modifier = Modifier.height(8.dp))
                ProfileLine(title = "Buts marqués", content = goalsCount.toString() )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerProfilePreview() {
    PlayerProfileContent(
        firstname = "John",
        lastname = "Doe",
        email = "john.doe@example.com",
        birthdate = "01/01/1990",
        phone = "0123456789",
        team = "Équipe A",
        convocationsCount = 5,
        presencesCount = 3,
        goalsCount = 4
    )
}

