package com.devid_academy.coachtrackercompose.ui.screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.R
import com.devid_academy.coachtrackercompose.data.manager.AuthManager
import com.devid_academy.coachtrackercompose.data.manager.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val authManager: AuthManager
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _direction = MutableStateFlow<String?>(null)
    val direction: StateFlow<String?> = _direction

    init {
        verifyToken()
    }

    fun verifyToken() {
        val token = preferencesManager.getToken()
        viewModelScope.launch {
            _isLoading.value = true
            delay(3000)
            _direction.value =
                if(token.isNullOrEmpty() || !authManager.isTokenValid(token))
                    "login"
                else
                    "main"
            _isLoading.value = false
        }
    }
}