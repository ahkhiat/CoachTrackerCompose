package com.devid_academy.coachtrackercompose.ui.screen.team

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.data.manager.PreferencesManager
import com.devid_academy.coachtrackercompose.data.network.ApiService
import com.devid_academy.coachtrackercompose.data.dto.TeamDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val api: ApiService,
    private val preferencesManager: PreferencesManager
    ): ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _teamStateFlow = MutableStateFlow<TeamDTO?>(null)
    val teamStateFlow : StateFlow<TeamDTO?> = _teamStateFlow

    init {
        getTeam()
    }
    fun getTeam() {
        viewModelScope.launch {
            _isLoading.value = true
            _teamStateFlow.value = null
            val teamId = preferencesManager.getTeamId()
            Log.i("TEAM VM", "Team id recuperé dans le PM : $teamId")
            try {
                val result = withContext(Dispatchers.IO) {
                    api.getApi().getTeam(teamId)
                }
                Log.i("VM TEAM", "VM TEAM : $result")
                _teamStateFlow.value = result
            } catch (e : Exception) {
                Log.e("VM TEAM", "Erreur API : ${e.message}", e)
            }
            _isLoading.value = false
        }
    }
    fun onLogout() {
        _teamStateFlow.value = null
    }


}