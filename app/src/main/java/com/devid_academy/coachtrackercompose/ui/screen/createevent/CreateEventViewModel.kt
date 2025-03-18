package com.devid_academy.coachtrackercompose.ui.screen.createevent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.R
import com.devid_academy.coachtrackercompose.data.dto.CreateEventDTO
import com.devid_academy.coachtrackercompose.data.dto.SeasonDTO
import com.devid_academy.coachtrackercompose.data.dto.StadiumDTO
import com.devid_academy.coachtrackercompose.data.dto.VisitorTeamDTO
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
class CreateEventViewModel  @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {


    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _visitorTeamList = MutableStateFlow<List<VisitorTeamDTO>>(emptyList())
    val visitorTeamList: StateFlow<List<VisitorTeamDTO>> = _visitorTeamList

    private val _stadiumList = MutableStateFlow<List<StadiumDTO>>(emptyList())
    val stadiumList: StateFlow<List<StadiumDTO>> = _stadiumList

    private val _seasonList = MutableStateFlow<List<SeasonDTO>>(emptyList())
    val seasonList: StateFlow<List<SeasonDTO>> = _seasonList

    private val _createSharedFlow = MutableSharedFlow<ViewModelEvent?>()
    val createSharedFlow: SharedFlow<ViewModelEvent?> = _createSharedFlow

    init {
        getVisitorTeamList()
        getStadiumList()
        getSeasonList()
    }

    fun createEvent(
        eventType: Int,
        date: String,
        stadium: Int,
        season: Int,
        visitorTeam: Int
    ) {
        //team ID
        viewModelScope.launch {

            if (eventType != null && stadium != null && season != null && !date.isNullOrBlank()) {
                val teamId = pm.getTeamId()
                Log.i(
                    "VM CREATE", "EVENT créée : type: $eventType, date: $date , " +
                            "équipe: $teamId, lieu: $stadium, saison: $season"
                )
                try {
                    val response = withContext(Dispatchers.IO) {
                        api.getApi().insertEvent(
                            CreateEventDTO(
                                date, eventType, teamId, visitorTeam,
                                stadium, season
                            )
                        )
                    }
                    if (response.isSuccessful) {
                        _createSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.create_success))
                        _createSharedFlow.emit(ViewModelEvent.NavigateToMainScreen)
                    } else when (response.code()) {
                        400 -> {
                            Log.i("VM LOGIN", "Erreur paramètre")
                            _createSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.error_param))
                        }

                        500 -> {
                            Log.i("VM LOGIN", "Erreur 500 Erreur serveur")
                            _createSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.server_error))
                        }

                    }

                } catch (e: Exception) {
                    Log.e("Error CREATE VM", "Erreur Create VM : ${e.message}")
                }
            } else {
                _createSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.fill_all_inputs))
            }
        }
    }

    fun getVisitorTeamList() {
        viewModelScope.launch {
            _isLoading.value = true
            val visitorTeamList = withContext(Dispatchers.IO) {
                api.getApi().getVisitorTeamList()
            }
            _visitorTeamList.emit(visitorTeamList)
            Log.i("VIEW MODEL SPINNER", "LISTE DES VISITOR TEAM: ${_visitorTeamList .value}")
            _isLoading.value = false
        }
    }

    fun getStadiumList() {
        viewModelScope.launch {
            _isLoading.value = true
            val stadiumList = withContext(Dispatchers.IO) {
                api.getApi().getStadiumList()
            }
            _stadiumList.emit(stadiumList)
            Log.i("VIEW MODEL SPINNER", "LISTE DES STADIUM: ${_stadiumList .value}")
            _isLoading.value = false
        }
    }

    fun getSeasonList(){
        viewModelScope.launch {
            _isLoading.value = true
            val seasonList = withContext(Dispatchers.IO) {
                api.getApi().getSeasonList()
            }
            _seasonList.emit(seasonList)
            Log.i("VIEW MODEL SPINNER", "LISTE DES SEASON: ${_seasonList .value}")
            _isLoading.value = false
        }
    }

}