package com.devid_academy.coachtrackercompose.ui.screen.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devid_academy.coachtrackercompose.R
import com.devid_academy.coachtrackercompose.ui.navigation.Screen
import com.devid_academy.coachtrackercompose.ui.screen.components.BlueButton
import com.devid_academy.coachtrackercompose.ui.screen.components.InputFormTextField
import com.devid_academy.coachtrackercompose.ui.theme.CoachTrackerColor
import com.devid_academy.coachtrackercompose.util.ViewModelEvent

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel) {

    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    LaunchedEffect(true) {
        loginViewModel.loginSharedFlow.collect { event ->
            when (event) {
                is ViewModelEvent.NavigateToMainScreen -> {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
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
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            LoginContent(
                onLogin = { login, mdp ->
                    loginViewModel.verifyLogin(login, mdp)
                    keyboardController?.hide()
                },
                onNavigate = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
    }

}

@Composable
fun LoginContent(
    onLogin: (email: String, password: String) -> Unit,
    onNavigate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        val context = LocalContext.current

        var emailForm by remember { mutableStateOf("") }
        var passwordFrom by remember { mutableStateOf("") }

        Text(
            text = context.getString(R.string.login_tv_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(100.dp))
        InputFormTextField(
            value = emailForm,
            onValueChange = { emailForm = it },
            label = context.getString(R.string.login_et_name)
        )
        Spacer(modifier = Modifier.height(15.dp))
        InputFormTextField(
            value = passwordFrom,
            onValueChange = { passwordFrom = it },
            label = context.getString(R.string.login_et_password),
            visualTransformation = true
        )
        Spacer(modifier = Modifier.height(200.dp))
        BlueButton(
            buttonText = "Sign in",
            onClick = {
                onLogin(emailForm, passwordFrom)
            },
            modifier = Modifier
                .width(160.dp)
                .height(50.dp),

        )
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = context.getString(R.string.login_tv_not_registered),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                onNavigate()
            }
        )
    }
}

@Composable
@Preview
fun LoginPreview() {
    LoginContent(
        onLogin = {_, _ -> },
        onNavigate = {}
    )
}
