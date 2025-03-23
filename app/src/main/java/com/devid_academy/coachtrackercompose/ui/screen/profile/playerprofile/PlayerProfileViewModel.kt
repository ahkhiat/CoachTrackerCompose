package com.devid_academy.coachtrackercompose.ui.screen.profile.playerprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.R
import com.devid_academy.coachtrackercompose.data.dto.PlayerProfileDTO
import com.devid_academy.coachtrackercompose.data.dto.UserProfileDTO
import com.devid_academy.coachtrackercompose.data.manager.PreferencesManager
import com.devid_academy.coachtrackercompose.data.network.ApiService
import com.devid_academy.coachtrackercompose.util.ViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlayerProfileViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _playerStateFlow = MutableStateFlow(PlayerProfileDTO())
    val playerStateFlow: StateFlow<PlayerProfileDTO> = _playerStateFlow

    private val _playerProfileSharedFlow = MutableSharedFlow<ViewModelEvent?>()
    val playerProfileSharedFlow: SharedFlow<ViewModelEvent?> = _playerProfileSharedFlow

    fun getPlayerProfile(userId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.getApi().getPlayerProfile(userId)
                }
                if(response.isSuccessful) {
                    _isLoading.value = false
                    val result = response.body()
                    result?.let {
                        _playerStateFlow.value = it
                        Log.i("VM PLAYER PROFILE", "Player Profile : $result")
                    }
                } else when (response.code()) {
                    404 -> {
                        _isLoading.value = false
                        Log.e("VM PLAYER PROFILE", "Player not found")
                        _playerProfileSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.player_not_found))
                    }
                    500 -> {
                        _isLoading.value = false
                        Log.e("VM PLAYER PROFILE", "Server error")
                        _playerProfileSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.server_error))
                    }
                }
            } catch (e : Exception) {
                _isLoading.value = false
                Log.e("VM PLAYER PROFILE", "Erreur API : ${e.message}", e)
            }
            _isLoading.value = false
        }
    }
}