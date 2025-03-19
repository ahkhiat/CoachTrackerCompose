package com.devid_academy.coachtrackercompose.ui.screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devid_academy.coachtrackercompose.ui.theme.CoachTrackerColor

@Composable
fun InputFormTextField(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        visualTransformation: Boolean = false,
        keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if(visualTransformation) PasswordVisualTransformation()
                                    else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@Composable
fun BlueButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    width: Int = 200,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.width(width.dp),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(
        containerColor = Color.Blue
        )
        ) {
            Text(
                text = buttonText,
                fontSize = 18.sp,
                color = Color.White
            )
        }
}
