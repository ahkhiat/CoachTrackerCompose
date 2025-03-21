package com.devid_academy.coachtrackercompose.ui.screen.presence.createpresence

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.R
import com.devid_academy.coachtrackercompose.data.dto.CreateConvocationDTO
import com.devid_academy.coachtrackercompose.data.dto.CreatePresenceDTO
import com.devid_academy.coachtrackercompose.data.dto.TeamDTO
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
class CreatePresenceViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {

    private val _teamStateFlow = MutableStateFlow<TeamDTO?>(null)
    val teamStateFlow : StateFlow<TeamDTO?> = _teamStateFlow

    private val _createPresenceSharedFlow = MutableSharedFlow<ViewModelEvent?>()
    val createPresenceSharedFlow: SharedFlow<ViewModelEvent?> = _createPresenceSharedFlow

    init {
        getTeam()
    }
    fun getTeam() {
        viewModelScope.launch {
            _teamStateFlow.value = null
            val teamId = pm.getTeamId()
            Log.i("CREATE Presence VM", "Team id recuperé dans le PM : $teamId")
            try {
                val result = withContext(Dispatchers.IO) {
                    api.getApi().getTeam(teamId)
                }
                Log.i("CREATE Presence VM", "VM TEAM : $result")
                _teamStateFlow.value = result
            } catch (e : Exception) {
                Log.e("CREATE Presence VM", "Erreur API : ${e.message}", e)
            }
        }
    }

    fun insertPresences(eventId: Int, playersList: List<Int>) {
        viewModelScope.launch {
            if (playersList.isNotEmpty()) {
                val response = withContext(Dispatchers.IO) {
                    api.getApi().insertPresence(
                        CreatePresenceDTO(
                            eventId,
                            playersList
                        )
                    )
                }
                if (response.isSuccessful) {
                    _createPresenceSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.presences_saved) )
                    _createPresenceSharedFlow.emit(ViewModelEvent.NavigateToMainScreen)
                } else when (response.code()){
                    400 -> _createPresenceSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.invalid_request))
                    500 -> _createPresenceSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.error_param))
                }


            }
        }

    }
}