package com.devid_academy.coachtrackercompose.ui.screen.convocation.editconvocation

import android.util.Log
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.devid_academy.coachtrackercompose.ui.navigation.BottomBar
import com.devid_academy.coachtrackercompose.ui.screen.components.BlueButton
import com.devid_academy.coachtrackercompose.ui.screen.convocation.createconvocation.PlayerItem
import com.devid_academy.coachtrackercompose.util.ViewModelEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditConvocationScreen(
    navController: NavController,
    editConvocationViewModel: EditConvocationViewModel,
    eventId: Int
) {
    val teamStateFlow by editConvocationViewModel
        .teamStateFlow.collectAsState()

//    val selectedPlayers = remember {
//        mutableStateOf(mutableSetOf<Int>())
//    }
    val convocationListStateFlow by editConvocationViewModel
        .convocationsListStateFlow.collectAsState()

    val convocatedPlayers = convocationListStateFlow?.mapNotNull {
        it?.player?.id
    }?.toSet() ?: emptySet()

    val selectedPlayers = remember {
        mutableStateOf(convocatedPlayers.toMutableSet())
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(convocationListStateFlow) {
        selectedPlayers.value = convocatedPlayers.toMutableSet()
    }

    LaunchedEffect(eventId) {
        editConvocationViewModel.getEvent(eventId)
    }

    LaunchedEffect(true) {
        editConvocationViewModel.editConvocationSharedFlow.collect { event ->
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
                        text = "Modifier convocations",
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
                    Log.i("EDIT CONVOC SCREEN", "Team : ${it.name}, playersList : ${it.players}")

                    EditConvocationContent(
                        teamName = it.name,
                        playersList = it.players,
                        selectedPlayers = selectedPlayers,
                        onValidate = { playersList ->
                            Log.i("onVALIDATE SCREEN", "Liste : $playersList")
                            editConvocationViewModel.updateConvocations(eventId, playersList)
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun EditConvocationContent(
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
                            player.id?.let { playerId ->
                                val updatedSelection = selectedPlayers.value.toMutableSet()
                                if (!updatedSelection.add(playerId)) {
                                    updatedSelection.remove(playerId)
                                }
                                selectedPlayers.value = updatedSelection.toMutableSet()
                            }
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

@Preview
@Composable
fun EditConvocationPreview() {

}