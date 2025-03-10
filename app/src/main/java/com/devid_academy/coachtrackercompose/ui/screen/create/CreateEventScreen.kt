package com.devid_academy.coachtrackercompose.ui.screen.create

import android.app.DatePickerDialog
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.devid_academy.coachtrackercompose.data.dto.SeasonDTO
import com.devid_academy.coachtrackercompose.data.dto.StadiumDTO
import com.devid_academy.coachtrackercompose.data.dto.VisitorTeamDTO
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CreateEventScreen(
    navController: NavController,
    createEventViewModel: CreateEventViewModel
) {
    val visitorTeamList by createEventViewModel.visitorTeamList.collectAsState()
    val stadiumList by createEventViewModel.stadiumList.collectAsState()
    val seasonList by createEventViewModel.seasonList.collectAsState()

    val visitorDropdownItems = visitorTeamList.mapNotNull {
        it.id?.let {
            id -> DropdownItem(id, it.club.name)
        }
    }

    val stadiumDropdownItems = stadiumList.mapNotNull {
        it.id?.let {
            id -> DropdownItem(id, it.name)
        }
    }

    val seasonDropdownItems = seasonList.mapNotNull {
        it.id?.let {
            id -> DropdownItem(id, it.name)
        }
    }

    CreateEventContent(
        visitorDropdownItems = visitorDropdownItems,
        stadiumDropdownItems = stadiumDropdownItems,
        seasonDropdownItems = seasonDropdownItems,
        onCreateEvent = { eventType, date, stadium, season, visitorTeam ->
            createEventViewModel.createEvent(
                eventType = eventType,
                date = date,
                stadium = stadium,
                season = season,
                visitorTeam = visitorTeam
            )
        },
        onNavigate = {
            navController.popBackStack()
        }
    )
}


@Composable
fun CreateEventContent(
    visitorDropdownItems: List<DropdownItem>,
    stadiumDropdownItems: List<DropdownItem>,
    seasonDropdownItems: List<DropdownItem>,
    onCreateEvent: (eventType: Int, date: String, stadium: Int,
                    season: Int, visitorTeam: Int) -> Unit,
    onNavigate: () -> Unit

) {
    var selectedDate by remember { mutableStateOf("") }
    var selectedTeam by remember { mutableStateOf<DropdownItem?>(null) }
    var selectedStadium by remember { mutableStateOf<DropdownItem?>(null) }
    var selectedSeason by remember { mutableStateOf<DropdownItem?>(null) }
    var eventType by remember { mutableStateOf(1) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = "Type d'événement :",
            fontWeight = FontWeight.Bold
        )
        Row {
            RadioButton(
                selected = eventType == 4,
                onClick = {
                    eventType = 4
                }
            )
            Text(
                text = "Entraînement",
                modifier = Modifier.clickable {
                    eventType = 4
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = eventType == 5,
                onClick = {
                    eventType = 5
                }
            )
            Text(
                text= "Match",
                modifier = Modifier.clickable {
                    eventType = 5
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        DatePickerField(
            selectedDate = selectedDate,
            onDateSelected = {
                date -> selectedDate = date
            }
        )
        if (eventType == 5) {
            SpinnerDropdown(
                items = visitorDropdownItems,
                selectedItem = selectedTeam,
                label = "Équipe visiteuse",
                onItemSelected = {
                     selectedTeam = it
                }
            )
        }
        SpinnerDropdown(
            items = stadiumDropdownItems,
            selectedItem = selectedStadium,
            label = "Stade",
            onItemSelected = {
                selectedStadium = it
            }
        )
        SpinnerDropdown(
            items = seasonDropdownItems,
            selectedItem = selectedSeason,
            label = "Saison",
            onItemSelected = {
                selectedSeason = it
            }
        )
        Button(
            onClick = {
                    onCreateEvent(
                        eventType,
                        selectedDate,
                        selectedStadium!!.id,
                        selectedSeason!!.id,
                        selectedTeam?.id ?: -1
                    )
                onNavigate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text= "Créer l'événement"
            )
        }
    }
}

@Composable
fun DatePickerField(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var showDialog by remember { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    Box {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = { Text("Date du match") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Sélectionner une date")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (showDialog) {
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, month, dayOfMonth)
                    onDateSelected(dateFormat.format(selectedCalendar.time))
                    showDialog = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}


@Composable
fun SpinnerDropdown(
    items: List<DropdownItem>,
    selectedItem: DropdownItem?,
    label: String,
    onItemSelected: (DropdownItem) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label, fontWeight = FontWeight.Bold)

        Box {
            OutlinedTextField(
                value = selectedItem?.name ?: "",
                onValueChange = {},
                label = { Text(label) },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Icon",
                        modifier = Modifier.clickable {
                            expanded = !expanded
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = true
                    }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                                   },
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item.name
                            )
                        },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

data class DropdownItem(
    val id: Int,
    val name: String
)


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CreateEventScreenPreview() {
   CreateEventContent(
       visitorDropdownItems = emptyList(),
       stadiumDropdownItems = emptyList(),
       seasonDropdownItems = emptyList(),
       onCreateEvent = { eventType, date, stadium, season, visitorTeam ->
           // Fake event creation for preview
           println("Event created with: $eventType, $date, $stadium, $season, $visitorTeam")
       },
       onNavigate = {}
   )
}
