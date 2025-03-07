package com.devid_academy.coachtrackercompose.ui.screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun InputFormTextField(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        visualTransformation: Boolean? = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if(visualTransformation!!) PasswordVisualTransformation()
                                    else VisualTransformation.None
    )
}