package com.devid_academy.coachtrackercompose.ui.screen.profile.privateprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.data.dto.UserProfileDTO
import com.devid_academy.coachtrackercompose.data.manager.PreferencesManager
import com.devid_academy.coachtrackercompose.data.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivateProfileViewModel @Inject constructor(
    private val api: ApiService,
    private val preferencesManager: PreferencesManager
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _userStateFlow = MutableStateFlow(UserProfileDTO())
    val userStateFlow: StateFlow<UserProfileDTO> = _userStateFlow

    private val _teamNameStateFlow = MutableStateFlow<String>("")
    val teamNameStateFlow: StateFlow<String> = _teamNameStateFlow

    init {
        getUserPofile()
    }

    fun getUserPofile() {

        _userStateFlow.value = preferencesManager.getUser()!!

        viewModelScope.launch {
            _isLoading.value = true
            try {
//                val result = withContext(Dispatchers.IO) {
//                    api.getApi().getUserProfile()
//                }
                val result = preferencesManager.getUser()
                _userStateFlow.value = result!!
                _teamNameStateFlow.value = (result.isCoachOf?.name ?: result.playsIn?.name).toString()
                Log.i("VM PROFILE", "VM COACH OU PLAYSIN : ${result.isCoachOf?.name}")

            } catch (e : Exception) {
                Log.e("VM PROFILE", "Erreur API : ${e.message}", e)
            }
            _isLoading.value = false
        }
    }




}