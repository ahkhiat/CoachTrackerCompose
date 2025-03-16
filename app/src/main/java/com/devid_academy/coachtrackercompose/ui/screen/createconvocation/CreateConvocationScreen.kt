package com.devid_academy.coachtrackercompose.ui.screen.createconvocation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.devid_academy.coachtrackercompose.ui.screen.components.GreenButton
import com.devid_academy.coachtrackercompose.ui.theme.CoachTrackerColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateConvocationScreen(
    navController: NavController,
    createConvocationViewModel: CreateConvocationViewModel
) {
    val teamStateFlow by createConvocationViewModel.teamStateFlow.collectAsState()

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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
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
                teamStateFlow?.let {
                    CreateConvocationContent(
                        teamName = it.name,
                        playersList = it.players,
                    )
                }
            }
        }
    )
}



@Composable
fun CreateConvocationContent(
    teamName: String,
    playersList: List<PlayerDTO>?,
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

        Text(
            text = "Joueurs",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        playersList?.let { players ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(players) { player ->
                    PlayerItem(player)
                }
            }
        } ?: Text(
            text = "Aucun joueur trouvé",
            style = MaterialTheme.typography.bodyMedium
        )

        GreenButton(
            buttonText = "Valider"
        ) {
            
        }
    }
}

@Composable
fun PlayerItem(player: PlayerDTO) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            ,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = player.user!!.firstname,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun CreateConvocationPreview() {
    val players = listOf(
        PlayerDTO(1, UserDTO(1, "Alice", "Durand"), null),
        PlayerDTO(2, UserDTO(2, "Emma", "Lemoine"), null),
        PlayerDTO(3, UserDTO(3, "Lucas", "Morel"), null),
        PlayerDTO(4, UserDTO(4, "Hugo", "Garcia"), null),
        PlayerDTO(5, UserDTO(5, "Léa", "Martinez"), null),
        PlayerDTO(6, UserDTO(6, "Noah", "Bernard"), null),
        PlayerDTO(7, UserDTO(7, "Chloé", "Dubois"), null),
        PlayerDTO(8, UserDTO(8, "Nathan", "Robert"), null),
        PlayerDTO(9, UserDTO(9, "Manon", "Richard"), null),
        PlayerDTO(10, UserDTO(10, "Tom", "Petit"), null),
        PlayerDTO(11, UserDTO(11, "Camille", "Lefebvre"), null),
        PlayerDTO(12, UserDTO(12, "Mathis", "Lemoine"), null),
        PlayerDTO(13, UserDTO(13, "Juliette", "Simon"), null),
        PlayerDTO(14, UserDTO(14, "Enzo", "Laurent"), null),
        PlayerDTO(15, UserDTO(15, "Lina", "Roux"), null)
    )

//    val coaches = listOf(
//        CoachDTO(
//            id = 1,
//            user = UserDTO(id = 3, firstname = "Sophie", lastname = "Martin")
//        )
//    )

   CreateConvocationContent(
        teamName = "U11F1",
        playersList = players
    )
}