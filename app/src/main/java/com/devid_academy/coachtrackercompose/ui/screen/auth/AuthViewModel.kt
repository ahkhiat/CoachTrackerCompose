package com.devid_academy.coachtrackercompose.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.devid_academy.coachtrackercompose.data.manager.AuthManager
import com.devid_academy.coachtrackercompose.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authManager: AuthManager,
): ViewModel() {


    fun logout() {
        authManager.logout()
    }

    fun isTokenValid(token: String?): Boolean {
        return authManager.isTokenValid(token)
    }

}