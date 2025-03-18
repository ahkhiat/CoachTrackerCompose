package com.devid_academy.coachtrackercompose.ui.screen.createconvocation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.R
import com.devid_academy.coachtrackercompose.data.dto.CreateConvocationDTO
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
class CreateConvocationViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {

    private val _teamStateFlow = MutableStateFlow<TeamDTO?>(null)
    val teamStateFlow : StateFlow<TeamDTO?> = _teamStateFlow

    private val _createConvocationSharedFlow = MutableSharedFlow<ViewModelEvent?>()
    val createConvocationSharedFlow:  SharedFlow<ViewModelEvent?> = _createConvocationSharedFlow

    init {
        getTeam()
    }
    fun getTeam() {
        viewModelScope.launch {
            _teamStateFlow.value = null
            val teamId = pm.getTeamId()
            Log.i("CREATE CONV VM", "Team id recuperé dans le PM : $teamId")
            try {
                val result = withContext(Dispatchers.IO) {
                    api.getApi().getTeam(teamId)
                }
                Log.i("CREATE CONV VM", "VM TEAM : $result")
                _teamStateFlow.value = result
            } catch (e : Exception) {
                Log.e("CREATE CONV VM", "Erreur API : ${e.message}", e)
            }
        }
    }

    fun insertConvocations(eventId: Int, playersList: List<Int>) {
        viewModelScope.launch {
            if (playersList.isNotEmpty()) {
                val response = withContext(Dispatchers.IO) {
                    api.getApi().insertConvocation(
                        CreateConvocationDTO(
                            eventId,
                            playersList
                        )
                    )
                }
                if (response.isSuccessful) {
                    _createConvocationSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.convocations_sended) )
                    _createConvocationSharedFlow.emit(ViewModelEvent.NavigateToMainScreen)
                } else when (response.code()){
                    400 -> _createConvocationSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.invalid_request))
                    500 -> _createConvocationSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.error_param))
                }


            }
        }

    }

}