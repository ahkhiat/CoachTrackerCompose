package com.devid_academy.coachtrackercompose.ui.screen.goal

import android.util.Log
import android.widget.ScrollView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devid_academy.coachtrackercompose.data.dto.GoalDTO
import com.devid_academy.coachtrackercompose.data.dto.PlayerDTO
import com.devid_academy.coachtrackercompose.data.dto.TeamDTO
import com.devid_academy.coachtrackercompose.data.dto.UserDTO
import com.devid_academy.coachtrackercompose.ui.navigation.BottomBar
import com.devid_academy.coachtrackercompose.ui.screen.components.BlueButton
import com.devid_academy.coachtrackercompose.ui.screen.components.InputFormTextField
import com.devid_academy.coachtrackercompose.ui.screen.convocation.createconvocation.CreateConvocationContent
import com.devid_academy.coachtrackercompose.ui.screen.convocation.createconvocation.PlayerItem
import com.devid_academy.coachtrackercompose.util.ViewModelEvent
import getPartialDate

@Composable
fun CreateGoalScreen(
    navController: NavController,
    createGoalViewModel: CreateGoalViewModel,
    eventId: Int
) {
    val teamStateFlow by createGoalViewModel.teamStateFlow.collectAsState()
    val selectedPlayer = remember {
        mutableStateOf<Int?>(null)
    }
    val goalsListStateFlow by createGoalViewModel.goalsListStateFlow.collectAsState()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val context = LocalContext.current

    LaunchedEffect(eventId) {
        createGoalViewModel.goalsList(eventId)
    }
    LaunchedEffect(true) {
        createGoalViewModel.createGoalSharedFlow.collect { event ->
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

    CreateGoalContent(
        snackbarHostState = snackbarHostState,
        navController = navController,
        teamStateFlow = teamStateFlow,
        selectedPlayer = selectedPlayer,
        goalsList = goalsListStateFlow,
        onValidate = { selectedPlayer, minuteGoal ->
            createGoalViewModel.insertGoal(eventId, selectedPlayer, minuteGoal)
        },
        onDelete = { goalId ->
            createGoalViewModel.deleteGoal(goalId, eventId)
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGoalContent(
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    teamStateFlow: TeamDTO?,
    selectedPlayer:  MutableState<Int?>,
    goalsList: List<GoalDTO>?,
    onValidate: (Int, Int) -> Unit,
    onDelete: (Int) -> Unit
) {
    var minuteGoal by remember { mutableStateOf(0) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mode Match",
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
                            imageVector = Icons.Default.ArrowBack,
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
                teamStateFlow?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    ) {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "Liste des buts :",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Column {
                                if (goalsList.isNullOrEmpty()) {
                                    Text(text = "Aucun but enregistré.", style = MaterialTheme.typography.bodyMedium)
                                } else {
                                    goalsList?.forEach { goal ->
                                        GoalCard(
                                            goal,
                                            onDelete = onDelete
                                        )
                                    }
                                }
                            }
                        }


                        Text(
                            text = "Qui a marqué ? :",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        it.players?.let { players ->
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
                                        isSelected = selectedPlayer.value == player.id,
                                        onClick = {
                                            selectedPlayer.value = if (selectedPlayer.value == player.id) null else player.id
                                        }
                                    )
                                }
                            }

                        } ?: Text(
                            text = "Aucun joueur trouvé",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Minute de jeu :",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        InputFormTextField(
                            value = minuteGoal.toString(),
                            onValueChange = {
                                minuteGoal = it.toIntOrNull() ?: 0
                            },
                            label = "Minute de jeu",
                            keyboardType = KeyboardType.Number
                        )

                        BlueButton(
                            buttonText = "Valider",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {
                                selectedPlayer.value?.let { playerId ->
                                    onValidate(
                                        playerId,
                                        minuteGoal
                                    )
                                }
                                Log.i("BUTTON", "Selected player: ${selectedPlayer.value}")
                            },
                            enabled = selectedPlayer.value != null
                        )
                    }
                }
            }
        }
    )
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

@Composable
fun GoalCard(
    goal: GoalDTO,
    onDelete: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(
                text = "⚽ ${goal.player.user.firstname} ${goal.player.user.lastname}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${goal.minuteGoal}\"",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            IconButton(onClick = { onDelete(goal.id) }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Supprimer",
                    tint = Color.Red
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun CreateGoalContentPreview() {
//
//
//}