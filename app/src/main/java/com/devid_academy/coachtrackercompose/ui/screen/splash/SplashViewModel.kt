package com.devid_academy.coachtrackercompose.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.data.manager.AuthManager
import com.devid_academy.coachtrackercompose.data.manager.PreferencesManager
import com.devid_academy.coachtrackercompose.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val authManager: AuthManager,
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _goToMainOrLogin = MutableSharedFlow<String?>()
    val goToMainOrLogin = _goToMainOrLogin.asSharedFlow()

    init {
        verifyToken()
    }

    fun verifyToken() {
        val token = preferencesManager.getToken()
        viewModelScope.launch {
            delay(300)
            _isLoading.value = true
            delay(3000)
            _goToMainOrLogin.emit(
                if(token.isNullOrEmpty() || !authManager.isTokenValid(token)) {
                    authManager.logout()
                    Screen.Login.route
                }
                else
                    Screen.Main.route
            )
            _isLoading.value = false
        }
    }
}