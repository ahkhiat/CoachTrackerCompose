package com.devid_academy.coachtrackercompose.ui.screen.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devid_academy.coachtrackercompose.data.dto.EventDTO
import formatDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    detailsViewModel: DetailsViewModel,
    eventId: Int
)  {
    val eventStateFlow by detailsViewModel.eventStateFlow.collectAsState()

    LaunchedEffect(eventId) {
        detailsViewModel.getEvent(eventId)
    }

    eventStateFlow?.let {
        DetailsContent(
            event = it,
            onNavigate = {
                navController.popBackStack()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsContent(
    event: EventDTO,
    onNavigate: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Détails de l'événement")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigate
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = event.team.name,
                fontSize = 32.sp
            )
            if(event.eventType.name == "Match") {
                Text(
                    text = "vs",
                    fontSize = 18.sp
                )
                event.visitorTeam?.club?.name?.let {
                    Text(
                        text = it,
                        fontSize = 32.sp
                    )
                }
            }
            Text(
                text = event.eventType.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Date : ${formatDate(event.date)}",
                fontSize = 18.sp
            )



            Text(
                text = "Lieu : ${event.stadium.name}",
                fontSize = 18.sp
            )

            Text(
                text = "Saison : ${event.season.name}",
                fontSize = 18.sp
            )
        }
    }
}