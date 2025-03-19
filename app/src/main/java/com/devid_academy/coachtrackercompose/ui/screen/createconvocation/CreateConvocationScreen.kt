package com.devid_academy.coachtrackercompose.ui.screen.createconvocation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.devid_academy.coachtrackercompose.data.dto.PlayerDTO
import com.devid_academy.coachtrackercompose.data.dto.UserDTO
import com.devid_academy.coachtrackercompose.ui.navigation.BottomBar
import com.devid_academy.coachtrackercompose.ui.navigation.Screen
import com.devid_academy.coachtrackercompose.ui.screen.components.BlueButton
import com.devid_academy.coachtrackercompose.util.ViewModelEvent

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateConvocationScreen(
    navController: NavController,
    createConvocationViewModel: CreateConvocationViewModel,
    eventId: Int
) {
    val teamStateFlow by createConvocationViewModel.teamStateFlow.collectAsState()
    val selectedPlayers = remember {
        mutableStateOf(mutableSetOf<Int>())
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current


    LaunchedEffect(true) {
        createConvocationViewModel.createConvocationSharedFlow.collect { event ->
            when (event) {
                is ViewModelEvent.NavigateToMainScreen -> {
//                    navController.navigate(Screen.Main.route) {
//                        popUpTo(Screen.CreateConvocation.route) {
//                            inclusive = true
//                        }
//                    }
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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Convocations",
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
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
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
                teamStateFlow?.let {
                    CreateConvocationContent(
                        teamName = it.name,
                        playersList = it.players,
                        selectedPlayers = selectedPlayers,
                        onValidate = { playersList ->
                            Log.i("onVALIDATE SCREEN", "Liste : $playersList")
                            createConvocationViewModel.insertConvocations(eventId, playersList)
                        }
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
    selectedPlayers: MutableState<MutableSet<Int>>,
    onValidate: (List<Int>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = teamName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,

        ){
            Text(
                text = "Joueurs convoqués : ${selectedPlayers.value.size}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray
                ),
                shape = RoundedCornerShape(5.dp),
                onClick = {
                    playersList?.let { list ->
//                        selectedPlayers.value = list.mapNotNull { it.id }.toMutableSet()
                        val updatedSelection = selectedPlayers.value.toMutableSet()
                        list.forEach { player ->
                            player.id?.let {
                                updatedSelection.add(it)
                            }
                        }
                        selectedPlayers.value = updatedSelection
                    }
                }
            ) {
                Text(
                    text = "Tous",
                    color = Color.Black
                    )
            }
        }

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
                    PlayerItem(
                        player,
                        isSelected = selectedPlayers.value.contains(player.id),
                        onClick = {
                            val updatedSelection = selectedPlayers.value.toMutableSet()
                            if (!updatedSelection.add(player.id!!)) {
                                updatedSelection.remove(player.id)
                            }
                            selectedPlayers.value = updatedSelection
                        }
                    )
                }
            }
        } ?: Text(
            text = "Aucun joueur trouvé",
            style = MaterialTheme.typography.bodyMedium
        )

        BlueButton(
            buttonText = "Valider",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                onValidate(selectedPlayers.value.toList())
                Log.i("BUTTON", "Selected players : ${selectedPlayers.value}")
            }
        )
    }
}

@Composable
fun PlayerItem(
    player: PlayerDTO,
    isSelected: Boolean,
    onClick: () -> Unit
    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.Green else Color.Gray
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if(isSelected) 0.dp else 2.dp)
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


@Preview(showBackground = true)
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

    val selectedPlayers = remember { mutableStateOf(mutableSetOf<Int>()) }

    CreateConvocationContent(
        teamName = "U11F1",
        playersList = players,
        selectedPlayers = selectedPlayers,
        onValidate = {}
    )
}