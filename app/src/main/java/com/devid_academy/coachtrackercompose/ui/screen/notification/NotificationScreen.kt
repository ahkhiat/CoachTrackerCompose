package com.devid_academy.coachtrackercompose.ui.screen.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devid_academy.coachtrackercompose.data.dto.EventDTO
import com.devid_academy.coachtrackercompose.ui.navigation.Screen
import com.devid_academy.coachtrackercompose.ui.screen.team.CoachItem
import com.devid_academy.coachtrackercompose.ui.screen.team.PlayerItem
import formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    notificationViewModel: NotificationViewModel
) {
    val eventWithoutConvocStateFlow by notificationViewModel.eventWithoutConvocList.collectAsState()

    LaunchedEffect(true) {
        notificationViewModel.notificationSharedFlow.collect { direction ->
            direction?.let {
                when {
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
                        text = "Notifications",
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
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Retour")
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
            eventWithoutConvocStateFlow?.let {
                NotificationContent(
                    eventList = it,
                    onClick = {
                        notificationViewModel.navigateToDetails(it.id)
                    }
                )
            }
        }
    }
}

@Composable
fun NotificationContent(
    eventList: List<EventDTO>? = emptyList(),
    onClick: (EventDTO) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0009FF), // Start color
                        Color(0xFF5358EC)  // End color
                    )
                )
            )
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            eventList?.let { event ->
                items(event) { event ->
                    NotificationCard(
                        event,
                        onClick = { onClick(it) }
                        )
                }
            } ?: item {
                Text(text = "Aucun notification", style = MaterialTheme.typography.bodyMedium)
            }

        }
    }

}

@Composable
fun NotificationCard(
    event: EventDTO,
    onClick: (EventDTO) -> Unit
    ) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme
                .surfaceVariant.copy(alpha = 0.1f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),
        onClick = {
            onClick(event)
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val eventType = if (event.eventType.name == "Match") "Le match" else "L'entrainement"
            Text(
                text = "${eventType} du ${formatDate(event.date)} n'a pas de convocations",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
fun NotificationPreview() {
//    NotificationContent()
}