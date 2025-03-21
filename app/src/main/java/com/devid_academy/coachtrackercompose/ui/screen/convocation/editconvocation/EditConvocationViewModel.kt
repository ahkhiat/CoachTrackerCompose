package com.devid_academy.coachtrackercompose.ui.screen.convocation.editconvocation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.R
import com.devid_academy.coachtrackercompose.data.dto.ConvocationDTO
import com.devid_academy.coachtrackercompose.data.dto.EventDTO
import com.devid_academy.coachtrackercompose.data.dto.TeamDTO
import com.devid_academy.coachtrackercompose.data.dto.UpdateConvocationDTO
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
class EditConvocationViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {

    private val _eventStateFlow = MutableStateFlow<EventDTO?>(null)
    val eventStateFlow: StateFlow<EventDTO?> = _eventStateFlow

    private val _teamStateFlow = MutableStateFlow<TeamDTO?>(null)
    val teamStateFlow : StateFlow<TeamDTO?> = _teamStateFlow

    private val _convocationsListStateFlow = MutableStateFlow<List<ConvocationDTO?>>(emptyList())
    val convocationsListStateFlow: StateFlow<List<ConvocationDTO?>> = _convocationsListStateFlow

    private val _editConvocationSharedFlow = MutableSharedFlow<ViewModelEvent?>()
    val editConvocationSharedFlow: SharedFlow<ViewModelEvent?> = _editConvocationSharedFlow

    init {
        getTeam()
    }

    fun getEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.getApi().getEvent(eventId)
                }
                if(response.isSuccessful) {
                    val result = response.body()
                    Log.i("VM EDIT CONVOC", "VM EDIT CONVOC result : $result")
                    _eventStateFlow.value = result
                    result?.convocations?.let {
//                        _showButtonCreateConvocationsStateFlow.value = it.isEmpty()
                        _convocationsListStateFlow.value = it
                        Log.i("VM EDIT CONVOC", "Liste convocations : ${convocationsListStateFlow.value}")
                    }
                } else when (response.code()){
                    500 -> Log.e("VM EDIT CONVOC", "VM DETAILS erreur 500")
                }
            } catch(e: Exception) {
                Log.e("VM EDIT CONVOC", "Erreur Catch: ${e.message}")
            }
        }
    }
    fun getTeam() {
        viewModelScope.launch {
            _teamStateFlow.value = null
            val teamId = pm.getTeamId()
            Log.i("VM EDIT CONVOC", "Team id recuperé dans le PM : $teamId")
            try {
                val result = withContext(Dispatchers.IO) {
                    api.getApi().getTeam(teamId)
                }
                Log.i("VM EDIT CONVOC", "VM TEAM : $result")
                _teamStateFlow.value = result
            } catch (e : Exception) {
                Log.e("VM EDIT CONVOC", "Erreur API : ${e.message}", e)
            }
        }
    }

    fun updateConvocations(eventId: Int, playersList: List<Int>) {
        viewModelScope.launch {
            if (playersList.isNotEmpty()) {
                val response = withContext(Dispatchers.IO) {
                    api.getApi().updateConvocation(
                        UpdateConvocationDTO(
                            eventId,
                            playersList
                        )
                    )
                }
                if (response.isSuccessful) {
                    _editConvocationSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.convocation_updated) )
                    _editConvocationSharedFlow.emit(ViewModelEvent.NavigateToMainScreen)
                } else when (response.code()){
                    400 -> _editConvocationSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.invalid_request))
                    500 -> _editConvocationSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.error_param))
                }
            }
        }
    }



}