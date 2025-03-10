package com.devid_academy.coachtrackercompose.ui.screen.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devid_academy.coachtrackercompose.R

@Composable
fun SplashScreen(navController: NavController, splashViewModel: SplashViewModel) {

    val isLoading by splashViewModel.isLoading.collectAsState()

    LaunchedEffect(true) {
        splashViewModel.goToMainOrLogin.collect { direction ->
            direction?.let {
                navController.navigate(it) {
                    popUpTo("splash") {
                        inclusive = true
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(700)) + slideOutVertically(
                animationSpec = tween(700),
                targetOffsetY = { -400 }
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.soccer_ball),
                contentDescription = "ballon",
                modifier = Modifier
                    .size(200.dp)
                    .padding(10.dp)
            )
        }

    }



}
