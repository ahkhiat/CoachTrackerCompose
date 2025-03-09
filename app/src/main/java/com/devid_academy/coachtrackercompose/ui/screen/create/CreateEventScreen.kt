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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CreateEventScreen(
    createEventViewModel: CreateEventViewModel
) {
    val visitorTeamList by createEventViewModel.visitorTeamList.collectAsState()
    val stadiumList by createEventViewModel.stadiumList.collectAsState()
    val seasonList by createEventViewModel.seasonList.collectAsState()

    CreateEventContent(
        visitorTeamList = visitorTeamList,
        stadiumList = stadiumList,
        seasonList = seasonList,
        onCreateEvent = { eventType, date, stadium, season, visitorTeam ->
            createEventViewModel.createEvent(
                eventType = eventType,
                date = date,
                stadium = stadium,
                season = season,
                visitorTeam = visitorTeam
            )
        }
    )
}

@Composable
fun CreateEventContent(
    visitorTeamList: List<VisitorTeamDTO>,
    stadiumList: List<StadiumDTO>,
    seasonList: List<SeasonDTO>,
    onCreateEvent: (eventType: Int, date: String, stadium: Int,
                    season: Int, visitorTeam: Int) -> Unit
) {
    var selectedDate by remember { mutableStateOf("") }
    var selectedTeam by remember { mutableStateOf<Int?>(null) }
    var selectedStadium by remember { mutableStateOf<Int?>(null) }
    var selectedSeason by remember { mutableStateOf<Int?>(null) }
    var eventType by remember { mutableStateOf(1) } //

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = "Type d'événement :",
            fontWeight = FontWeight.Bold
        )
        Row {
            RadioButton(
                selected = eventType == 1,
                onClick = {
                    eventType = 1
                }
            )
            Text(
                text = "Entraînement",
                modifier = Modifier.clickable {
                    eventType = 1
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = eventType == 2,
                onClick = {
                    eventType = 2
                }
            )
            Text(
                text= "Match",
                modifier = Modifier.clickable {
                    eventType = 2
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
        if (eventType == 2) {
            SpinnerDropdown(
                items = visitorTeamList,
                selectedItem = selectedTeam,
                label = "Équipe visiteuse",
                onItemSelected = {
//                     selectedTeam = it.id
                }
            )
        }
        SpinnerDropdown(
            items = stadiumList,
            selectedItem = selectedStadium,
            label = "Stade",
            onItemSelected = {
//                stadium -> selectedStadium = stadium.id
            }
        )
        SpinnerDropdown(
            items = seasonList,
            selectedItem = selectedSeason,
            label = "Saison",
            onItemSelected = {
//                season -> selectedSeason = season.id
            }
        )
        Button(
            onClick = {
                    onCreateEvent(
                        eventType,
                        selectedDate,
                        selectedStadium!!,
                        selectedSeason!!,
                        selectedTeam ?: -1
                    )

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

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

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
fun <T> SpinnerDropdown(
    items: List<T>,
    selectedItem: T?,
    label: String,
    onItemSelected: (T) -> Unit
) where T : Any {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label, fontWeight = FontWeight.Bold)

        Box {
            OutlinedTextField(
                value = selectedItem?.toString() ?: "",
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
                                text = item.toString()
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





//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun CreateEventContent(
//    visitorTeamList: List<VisitorTeamDTO>,
//    stadiumList: List<StadiumDTO>,
//    seasonList: List<SeasonDTO>,
//    onDateSelected: (String) -> Unit,
//    onTeamSelected: (VisitorTeamDTO) -> Unit,
//    onStadiumSelected: (StadiumDTO) -> Unit,
//    onSeasonSelected: (SeasonDTO) -> Unit
//) {
//
//    var selectedDate by remember { mutableStateOf("") }
//    var showDatePicker by remember { mutableStateOf(false) }
//
//    var selectedTeam by remember { mutableStateOf<VisitorTeamDTO?>(null) }
//    var selectedStadium by remember { mutableStateOf<StadiumDTO?>(null) }
//    var selectedSeason by remember { mutableStateOf<SeasonDTO?>(null) }
//    var eventType by remember { mutableStateOf(1) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        // 📅 Sélection de la date
//        OutlinedTextField(
//            value = selectedDate,
//            onValueChange = {},
//            label = { Text("Date du match") },
//            readOnly = true,
//            trailingIcon = {
//                IconButton(onClick = { showDatePicker = true }) {
//                    Icon(Icons.Default.DateRange, contentDescription = "Sélectionner une date")
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        if (showDatePicker) {
//            val context = LocalContext.current
//            val calendar = Calendar.getInstance()
//
//            DatePickerDialog(
//                context,
//                { _, year, month, day ->
//                    selectedDate = "$day/${month + 1}/$year"
//                    onDateSelected(selectedDate)
//                    showDatePicker = false
//                },
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//            ).show()
//        }
//
//        // 🔽 Dropdowns (Spinners)
//        DropdownSelector(
//            label = "Équipe visiteuse",
//            items = visitorTeamList,
//            selectedItem = selectedTeam,
//            onItemSelected = {
//                selectedTeam = it
//                onTeamSelected(it)
//            },
//            itemLabel = { it.club.name }
//        )
//
//        DropdownSelector(
//            label = "Stade",
//            items = stadiumList,
//            selectedItem = selectedStadium,
//            onItemSelected = {
//                selectedStadium = it
//                onStadiumSelected(it)
//            },
//            itemLabel = { it.name }
//        )
//
//        DropdownSelector(
//            label = "Saison",
//            items = seasonList,
//            selectedItem = selectedSeason,
//            onItemSelected = {
//                selectedSeason = it
//                onSeasonSelected(it)
//            },
//            itemLabel = { it.name }
//        )
//    }
//}

//@Composable
//fun <T> DropdownSelector(
//    label: String,
//    items: List<T>,
//    selectedItem: T?,
//    onItemSelected: (T) -> Unit,
//    itemLabel: (T) -> String
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    OutlinedTextField(
//        value = selectedItem?.let { itemLabel(it) } ?: "",
//        onValueChange = {},
//        label = { Text(label) },
//        readOnly = true,
//        modifier = Modifier.fillMaxWidth(),
//        trailingIcon = {
//            IconButton(onClick = { expanded = true }) {
//                Icon(Icons.Default.ArrowDropDown, contentDescription = "Ouvrir le menu")
//            }
//        }
//    )
//
//    DropdownMenu(
//        expanded = expanded,
//        onDismissRequest = { expanded = false }
//    ) {
//        items.forEach { item ->
//            DropdownMenuItem(
//                text = { Text(itemLabel(item)) },
//                onClick = {
//                    onItemSelected(item)
//                    expanded = false
//                }
//            )
//        }
//    }
//}
//
//

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CreateEventScreenPreview() {
   CreateEventContent(
       visitorTeamList = emptyList(),
       stadiumList = emptyList(),
       seasonList = emptyList(),
       onCreateEvent = { eventType, date, stadium, season, visitorTeam ->
           // Fake event creation for preview
           println("Event created with: $eventType, $date, $stadium, $season, $visitorTeam")
       }
   )
}
