package com.devid_academy.coachtrackercompose.ui.screen.auth

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.provider.Settings.Global.getString
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.devid_academy.coachtrackercompose.R
import com.devid_academy.coachtrackercompose.ui.navigation.Screen
import com.devid_academy.coachtrackercompose.ui.screen.components.BlueButton
import com.devid_academy.coachtrackercompose.ui.screen.components.InputFormTextField
import com.devid_academy.coachtrackercompose.ui.screen.event.createevent.DatePickerField
import com.devid_academy.coachtrackercompose.util.ViewModelEvent
import com.devid_academy.coachtrackercompose.util.makeToast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun RegisterScreen(navController: NavController, registerViewModel: RegisterViewModel) {

    val isLoading by registerViewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatedPassword by remember { mutableStateOf("") }
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        registerViewModel.registerSharedFlow.collect { event ->
            when (event) {
                is ViewModelEvent.NavigateToMainScreen -> {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Register.route) {
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

            RegisterContent(
                isLoading = isLoading,
                onRegister = { email, password, passwordConfirm, firstname, lastname, birthdate ->
                    registerViewModel.register(email, password, passwordConfirm, firstname, lastname, birthdate)
                    keyboardController?.hide()
                },
                onNavigate = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
    }
}

@Composable
fun RegisterContent(
    isLoading: Boolean,
    onRegister: (
        email: String,
        password: String,
        passwordConfirm: String,
        firstname: String,
        lastname: String,
        birthdate: String
        ) -> Unit,
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
        var passwordConfirmForm by remember { mutableStateOf("") }
        var firstnameForm by remember { mutableStateOf("") }
        var lastnameForm by remember { mutableStateOf("") }
        var birthdateForm by remember { mutableStateOf("") }

        Text(
            text = context.getString(R.string.register_tv_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(30.dp))
        InputFormTextField(
            value = emailForm,
            onValueChange = { emailForm = it },
            label = context.getString(R.string.login_et_name),
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(15.dp))
        InputFormTextField(
            value = passwordFrom,
            onValueChange = { passwordFrom = it },
            label = context.getString(R.string.login_et_password),
            visualTransformation = true
        )
        Spacer(modifier = Modifier.height(15.dp))
        InputFormTextField(
            value = passwordConfirmForm,
            onValueChange = { passwordConfirmForm = it },
            label = context.getString(R.string.register_et_password_confirm),
            visualTransformation = true
        )
        Spacer(modifier = Modifier.height(15.dp))
        InputFormTextField(
            value = firstnameForm,
            onValueChange = { firstnameForm = it },
            label = context.getString(R.string.register_et_firstname),
        )
        Spacer(modifier = Modifier.height(15.dp))
        InputFormTextField(
            value = lastnameForm,
            onValueChange = { lastnameForm = it },
            label = context.getString(R.string.register_et_lastname)
        )
        Spacer(modifier = Modifier.height(15.dp))
        BirthdayPickerField(
            selectedDate = birthdateForm,
            onDateSelected = {
                    date -> birthdateForm = date
            }
        )

        Spacer(modifier = Modifier.height(30.dp))
        BlueButton(
            buttonText = "Sign up",
            onClick = {
                onRegister(
                    emailForm,
                    passwordFrom,
                    passwordConfirmForm,
                    firstnameForm,
                    lastnameForm,
                    birthdateForm
                )
            },
            modifier = Modifier
                .width(160.dp)
                .height(50.dp),

            )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = context.getString(R.string.register_tv_already_registered),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                onNavigate()
            }
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                CircularProgressIndicator(color = Color.Blue)
            }
        }
    }
}
@Composable
fun BirthdayPickerField(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var showDialog by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Box {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = { Text("Date de naissance") },
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
@Preview(showBackground = true)
fun RegisterPreview() {
    RegisterContent(
        isLoading = false,
        onRegister = {_, _, _, _, _, _ -> },
        onNavigate = {}
    )
}
