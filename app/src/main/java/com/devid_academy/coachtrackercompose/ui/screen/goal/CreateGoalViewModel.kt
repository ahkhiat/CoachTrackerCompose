package com.devid_academy.coachtrackercompose.ui.screen.goal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.R
import com.devid_academy.coachtrackercompose.data.dto.CreateGoalDTO
import com.devid_academy.coachtrackercompose.data.dto.GoalDTO
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
class CreateGoalViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {

    private val _teamStateFlow = MutableStateFlow<TeamDTO?>(null)
    val teamStateFlow : StateFlow<TeamDTO?> = _teamStateFlow

    private val _goalsListStateFlow = MutableStateFlow<List<GoalDTO>?>(emptyList())
    val goalsListStateFlow : StateFlow<List<GoalDTO>?> = _goalsListStateFlow

    private val _createGoalSharedFlow = MutableSharedFlow<ViewModelEvent?>()
    val createGoalSharedFlow: SharedFlow<ViewModelEvent?> = _createGoalSharedFlow


    init {
        getTeam()
    }
    fun getTeam() {
        viewModelScope.launch {
            _teamStateFlow.value = null
            val teamId = pm.getTeamId()
            Log.i("CREATE GOAL VM", "Team id recuperé dans le PM : $teamId")
            try {
                val result = withContext(Dispatchers.IO) {
                    api.getApi().getTeam(teamId)
                }
                Log.i("CREATE GOAL VM", "Result : $result")
                _teamStateFlow.value = result
            } catch (e : Exception) {
                Log.e("CREATE GOAL VM", "Get TEAM Erreur API : ${e.message}", e)
            }
        }
    }

    fun goalsList(eventId: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.getApi().getGoals(eventId)
                }
                if(response.isSuccessful){
                    val result = response.body()
                    _goalsListStateFlow.value = result
                } else when (response.code()){
                    500 -> Log.e("VM CREATE GOAL", "GOAL LIST erreur 500")
                }
            } catch (e: Exception) {
                Log.e("CREATE GOAL VM", "GOALS LIST Erreur API : ${e.message}", e)
            }
        }
    }

    fun insertGoal(
        eventId: Int,
        playerId: Int,
        minuteGoal: Int
    ) {
        viewModelScope.launch {
            if (minuteGoal in 1..90) {
                val response = withContext(Dispatchers.IO) {
                    api.getApi().insertGoal(
                        CreateGoalDTO(
                            eventId,
                            playerId,
                            minuteGoal
                        )
                    )
                }
                if (response.isSuccessful) {
                    goalsList(eventId)
                    _createGoalSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.goal_saved) )
                } else when (response.code()){
                    400 -> _createGoalSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.invalid_request))
                    500 -> _createGoalSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.error_param))
                }
            } else {
                _createGoalSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.enter_valid_time) )
            }
        }
    }

    fun deleteGoal(goalId: Int, eventId: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.getApi().deleteGoal(goalId)
                }
                if (response.isSuccessful) {
                    goalsList(eventId)
                } else {
                    Log.e("VM DELETE GOAL", "Erreur lors de la suppression du but")
                }
            } catch (e: Exception) {
                Log.e("DELETE GOAL VM", "Erreur API : ${e.message}", e)
            }
        }
    }
}