package com.devid_academy.coachtrackercompose.ui.screen.main

import android.icu.text.SimpleDateFormat
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devid_academy.coachtrackercompose.data.dto.AgeCategoryDTO
import com.devid_academy.coachtrackercompose.data.dto.ClubDTO
import com.devid_academy.coachtrackercompose.data.dto.ConvocationDTO
import com.devid_academy.coachtrackercompose.data.dto.EventDTO
import com.devid_academy.coachtrackercompose.data.dto.EventTypeDTO
import com.devid_academy.coachtrackercompose.data.dto.PresenceDTO
import com.devid_academy.coachtrackercompose.data.dto.SeasonDTO
import com.devid_academy.coachtrackercompose.data.dto.StadiumDTO
import com.devid_academy.coachtrackercompose.data.dto.TeamDTO
import com.devid_academy.coachtrackercompose.data.dto.VisitorTeamDTO
import com.devid_academy.coachtrackercompose.ui.theme.EventWithoutConvocationsColor
import com.google.accompanist.swiperefresh.SwipeRefresh
import java.util.Locale

//@Composable
//fun EventContent(eventList: List<EventDTO>) {
//    LazyColumn() {
//        items(items = eventList) {
//            ItemView(
//                event = it
//            )
//        }
//    }
//}

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalMaterial3Api
@Composable
fun EventContent(
    eventList: List<EventDTO>,
    onClick: (EventDTO) -> Unit
    ) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(
                items = eventList,
                key = { it.id }
            ) { eventDTO ->
                ItemView(
                    event = eventDTO,
                    onClick = { onClick(it) }
                )
            }
        }
    }
}




@Composable
fun ItemView(
    event: EventDTO,
    onClick: (EventDTO) -> Unit
    ) {

    val dateString = event.date
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).parse(dateString)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if(event.hasConvocations == true)MaterialTheme.colorScheme.surface else EventWithoutConvocationsColor,
        ),
        modifier = Modifier
            .width(380.dp)
            .wrapContentHeight()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = {
            onClick(event)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(80.dp)
            ) {
                Text(
                    text = SimpleDateFormat("EEEE", Locale.getDefault()).format(date),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = SimpleDateFormat("dd", Locale.getDefault()).format(date),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = SimpleDateFormat("MMM", Locale.getDefault()).format(date),
                    fontSize = 14.sp
                )
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = if (event.eventType.name == "Match")
                        event.visitorTeam!!.club.name
                    else
                        event.eventType.name,
                    fontSize = if (event.eventType.name == "Match") 20.sp else 14.sp,
                    fontWeight = if (event.eventType.name == "Match")
                        FontWeight.Bold
                    else
                        FontWeight.Normal,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 10.dp)
                )
                Text(
                    text = event.stadium.name,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 10.dp)

                )
            }

            Icon(
                imageVector = if (event.eventType.name == "Match")
                                    Icons.Default.SportsSoccer
                              else
                                    Icons.Default.Event,
                contentDescription = "Event Type",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewItemView() {
    val fakeEvent = EventDTO(
        id = 0,
        date = "2024-03-07T15:30:00+01:00",
        eventType = EventTypeDTO(id = 0, name = "Match"),
        team = TeamDTO(
            id = 0,
            name = "U44H1",
            coaches = emptyList(),
            players = emptyList()
        ),
        visitorTeam = VisitorTeamDTO(
            id = 0,
            club = ClubDTO(
                id = 0,
                name = "Olympique de Marseille"),
                ageCategory = AgeCategoryDTO(
                    name = "U44")),
        stadium = StadiumDTO(
            id = 0,
            name = "Stade Vélodrome",
            adress = null,
            postalCode = null,
            town = null
        ),
        season = SeasonDTO(
            id = 0,
            name = "2024/2025"
        ),
        hasConvocations = false,
        convocations = emptyList(),
        presences = emptyList(),

    )

    ItemView(
        event = fakeEvent,
        onClick = {}
        )
}