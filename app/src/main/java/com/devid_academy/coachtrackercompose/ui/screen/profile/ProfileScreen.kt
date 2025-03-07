package com.devid_academy.coachtrackercompose.ui.screen.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.devid_academy.coachtrackercompose.ui.screen.auth.AuthViewModel


@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel
) {
    val teamName by profileViewModel.teamNameStateFlow.collectAsState()
    val userStateFlow by profileViewModel.userStateFlow.collectAsState()

    ProfileScreenContent(
        firstname = userStateFlow.firstname,
        lastname = userStateFlow.lastname,
        email = userStateFlow.email,
        birthdate = userStateFlow.birthdate,
        phone = userStateFlow.phone!!,
        team = teamName,
        onLogout = { authViewModel.logout() }
    )
}
@Composable
fun ProfileScreenContent(
    firstname: String,
    lastname: String,
    email: String,
    birthdate: String,
    phone: String,
    team: String,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "$firstname $lastname",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold)
            }
        }
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row() {
                    Text(
                        text = "Informations",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                ProfileLine(title = "Email", content = email)
                ProfileLine(title = "Birthdate", content = birthdate)
                ProfileLine(title = "Phone", content = phone)

            }
        }
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Team",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = team,
                    fontSize = 16.sp
                )
            }
        }
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.8f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                Button(onClick = onLogout, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                    Text(text = "Se déconnecter", color = Color.Red, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }    }
}

@Composable
fun ProfileLine(title: String, content: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Text(
                text = title,
                fontSize = 16.sp
            )
        }
        Column() {
            Text(
                text = content,
                fontSize = 16.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreenContent(
        firstname = "John",
        lastname = "Doe",
        email = "john.doe@example.com",
        birthdate = "01/01/1990",
        phone = "0123456789",
        team = "Équipe A",
        onLogout = {}
    )
}



