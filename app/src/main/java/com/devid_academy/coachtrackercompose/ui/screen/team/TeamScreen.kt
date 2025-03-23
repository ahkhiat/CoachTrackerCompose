package com.devid_academy.coachtrackercompose.ui.screen.team

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devid_academy.coachtrackercompose.data.dto.CoachDTO
import com.devid_academy.coachtrackercompose.data.dto.PlayerDTO
import com.devid_academy.coachtrackercompose.data.dto.UserDTO
import com.devid_academy.coachtrackercompose.ui.navigation.BottomBar
import com.devid_academy.coachtrackercompose.ui.navigation.Screen
import com.devid_academy.coachtrackercompose.ui.screen.main.EventContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen(
    navController: NavController,
    teamViewModel: TeamViewModel
) {
    var selectedPlayer by remember { mutableStateOf<PlayerDTO?>(null) }

    LaunchedEffect(true) {
        teamViewModel.teamSharedFlow.collect { direction ->
            direction?.let {
                when {
                    it.startsWith("player_profile/") -> {
                        navController.navigate(it)
                    }
                }
            }
        }
    }

    val teamStateFlow by teamViewModel.teamStateFlow.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Team",
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
                teamStateFlow?.let {
                    TeamContent(
                        teamName = it.name,
                        playersList = it.players,
                        coachesList = it.coaches,
                        onClick = {player ->
                            player.user.id?.let { userId ->
                                teamViewModel.navigateToPublicProfile(userId)
                            }
                        }
                    )
                }
            }
        }
    )


}



@Composable
fun TeamContent(
    teamName: String,
    playersList: List<PlayerDTO>?,
    coachesList: List<CoachDTO>?,
    onClick: (PlayerDTO) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = teamName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Coachs",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            coachesList?.let { coaches ->
                items(coaches) { coach ->
                    CoachItem(coach)
                }
            } ?: item {
                Text(text = "Aucun coach trouvé", style = MaterialTheme.typography.bodyMedium)
            }

            item {
                Text(
                    text = "Joueurs",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            playersList?.let { players ->
                items(players) { player ->
                    PlayerItem(
                        player,
                        onClick = {
                            onClick(player)
                        }
                    )
                }
            } ?: item {
                Text(text = "Aucun joueur trouvé", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun CoachItem(coach: CoachDTO) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = coach.user!!.firstname, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PlayerItem(
    player: PlayerDTO,
    onClick: (PlayerDTO) -> Unit
    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onClick(player) }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = player.user!!.firstname, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TeamPreview() {
    val players = listOf(
        PlayerDTO(
            id = 1,
            user = UserDTO(id = 1, firstname = "Alice", lastname = "Dupont"),
            playsInTeam = null
        ),
        PlayerDTO(
            id = 2,
            user = UserDTO(id = 2, firstname = "Emma", lastname = "Lemoine"),
            playsInTeam = null
        )
    )

    val coaches = listOf(
        CoachDTO(
            id = 1,
            user = UserDTO(id = 3, firstname = "Sophie", lastname = "Martin")
        )
    )

    TeamContent(
        teamName = "U11F1",
        playersList = players,
        coachesList = coaches,
        onClick = {}
    )
}