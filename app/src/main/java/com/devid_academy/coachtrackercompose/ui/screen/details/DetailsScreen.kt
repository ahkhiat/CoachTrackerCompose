package com.devid_academy.coachtrackercompose.ui.screen.details

import DatePattern
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devid_academy.coachtrackercompose.data.dto.ClubDTO
import com.devid_academy.coachtrackercompose.data.dto.ConvocationDTO
import com.devid_academy.coachtrackercompose.data.dto.EventDTO
import com.devid_academy.coachtrackercompose.data.dto.EventTypeDTO
import com.devid_academy.coachtrackercompose.data.dto.PlayerDTO
import com.devid_academy.coachtrackercompose.data.dto.SeasonDTO
import com.devid_academy.coachtrackercompose.data.dto.StadiumDTO
import com.devid_academy.coachtrackercompose.data.dto.TeamDTO
import com.devid_academy.coachtrackercompose.data.dto.UserDTO
import com.devid_academy.coachtrackercompose.data.dto.VisitorTeamDTO
import com.devid_academy.coachtrackercompose.ui.navigation.Screen
import com.devid_academy.coachtrackercompose.ui.screen.components.BlueButton
import com.devid_academy.coachtrackercompose.util.getStatus
import getPartialDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    detailsViewModel: DetailsViewModel,
    eventId: Int
)  {
    val eventStateFlow by detailsViewModel.eventStateFlow.collectAsState()
    val showButtonCreate by detailsViewModel.showButtonCreateConvocationsStateFlow.collectAsState()
    val convocationsList by detailsViewModel.convocationsListStateFlow.collectAsState()

    LaunchedEffect(eventId) {
        detailsViewModel.getEvent(eventId)
    }

    eventStateFlow?.let {
        DetailsContent(
            event = it,
            convocationsList = convocationsList,
            onNavigate = {
                navController.popBackStack()
            },
            showButtonCreate = showButtonCreate,
            onNavigateToCreateConvocations = {
                navController.navigate(Screen.CreateConvocation.route + "/$eventId")
            },
            onNavigateToEditConvocation = {
                navController.navigate(Screen.EditConvocation.route + "/$eventId")
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsContent(
    event: EventDTO,
    convocationsList: List<ConvocationDTO?>,
    onNavigate: () -> Unit,
    showButtonCreate: Boolean,
    onNavigateToCreateConvocations: () -> Unit,
    onNavigateToEditConvocation: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = getPartialDate(event.date, DatePattern.WeekDay) + " " +
                                getPartialDate(event.date, DatePattern.Day) + " " +
                                getPartialDate(event.date, DatePattern.Month)  + " " +
                                getPartialDate(event.date, DatePattern.Year),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigate
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarColors(
                    containerColor = Color.Blue,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    scrolledContainerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text(
                        text = event.team.name,
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    if(event.eventType.name == "Match") {
                        Text(
                            text = "vs",
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        event.visitorTeam?.club?.name?.let {
                            Text(
                                text = it,
                                fontSize = 30.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Rendez-vous ",
                fontSize = 18.sp
            )

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                ) {
                    Text(
                        text = getPartialDate(event.date, DatePattern.Hour, offsetHours = -1),
                        fontSize = 32.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = event.eventType.name,
                fontSize = 18.sp
            )
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),

                ) {
                    Text(
                        text = "Début à " + getPartialDate(event.date, DatePattern.Hour),
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Fin à " + getPartialDate(event.date, DatePattern.Hour, offsetHours = +2),
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Lieu :",
                fontSize = 18.sp
                )
            Text(
                text = event.stadium.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${event.stadium.adress} ${event.stadium.postalCode} ${event.stadium.town}",
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(20.dp))

            if (showButtonCreate) {
                BlueButton(
                    buttonText = "Convoquer mes joueurs",
                    width = 250,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        onNavigateToCreateConvocations()
                    }
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                    ,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Joueurs convoqués : " + convocationsList.size,
                        fontSize = 18.sp
                    )
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(5.dp),
                        onClick = {
                            onNavigateToEditConvocation()
                        }
                    ) {
                        Text(
                            text = "Modifier",
                            color = Color.Black
                        )
                    }

                }
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp),

                        ) {
                        convocationsList.forEach {convocation ->
                            convocation?.let {
                                Text(
                                    text = "${it.player.user.firstname} (${context.getStatus(it.status)})",
                                    fontSize = 18.sp
                                )
                            }
                        }

                    }
                }
            }



        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailsContent() {
    val sampleEvent = EventDTO(
        id = 0,
        date = "2025-03-20T09:00:00+00:00",
        eventType = EventTypeDTO(
            id = 0,
            name = "Match"
        ),
        team = TeamDTO(
            id = 0,
            name = "U11F1",
            coaches = emptyList(),
            players = emptyList()
        ),
        visitorTeam = VisitorTeamDTO(
            id = 0,
            club = ClubDTO(
                id = 0,
                name = "Olympique de Marseille"
            ),
            ageCategory = null
        ),
        stadium = StadiumDTO(
            id = 0,
            name = "Stade Exemple",
            adress = "18 rue des Lilas",
            postalCode = "13005",
            town = "Marseille"
        ),
        season = SeasonDTO(
            id = 0,
            name = "2024-2025"
        ),
        presences = emptyList(),
        convocations = emptyList(),
        hasConvocations = true
    )
    DetailsContent(
        event = sampleEvent,
        convocationsList = listOf(
            ConvocationDTO(
                status = 0,
                player = PlayerDTO(
                    id = 24,
                    UserDTO(
                        id = 52,
                        firstname = "Marie",
                        lastname = "Dubois"
                    ),
                    playsInTeam = null
                )
            )
        ),
        onNavigate = {},
        showButtonCreate = false,
        onNavigateToCreateConvocations = {},
        onNavigateToEditConvocation = {}
    )
}