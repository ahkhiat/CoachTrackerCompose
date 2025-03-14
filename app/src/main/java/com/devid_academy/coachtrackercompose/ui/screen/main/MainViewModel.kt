package com.devid_academy.coachtrackercompose.ui.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.data.dto.EventDTO
import com.devid_academy.coachtrackercompose.data.dto.EventTypeDTO
import com.devid_academy.coachtrackercompose.data.manager.PreferencesManager
import com.devid_academy.coachtrackercompose.data.network.ApiService
import com.devid_academy.coachtrackercompose.ui.navigation.Screen
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
class MainViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

//    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Idle)
//    val sessionState: StateFlow<SessionState> = _sessionState

//    private val _categories = MutableStateFlow<List<EventTypeDTO>>(emptyList())
//    val categories: StateFlow<List<EventTypeDTO>> = _categories

    private val _teamNameStateFlow = MutableStateFlow<String>("")
    val teamNameStateFlow: StateFlow<String> = _teamNameStateFlow

    private val _eventsStateFlow = MutableStateFlow<List<EventDTO>>(emptyList())
    val eventsStateFlow: StateFlow<List<EventDTO>> = _eventsStateFlow

    private val _mainSharedFlow = MutableSharedFlow<String?>()
    val mainSharedFlow: SharedFlow<String?> = _mainSharedFlow

    init {
        getEvents()
    }

    fun getEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = pm.getUser()
                val teamName = user?.isCoachOf?.name
                    ?: user?.playsIn?.name
                    ?: user?.isParentOf?.firstOrNull()?.playsIn?.name
                teamName?.let {
                    _teamNameStateFlow.value = it
                }
                Log.i("VM MAIN", "Team Name : $teamName")
                val response = withContext(Dispatchers.IO) {
                    api.getApi().getAllEvents(teamName!!)
                }
                if(response.isSuccessful) {
                    val result = response.body()
                    Log.i("VM MAIN", "VM MAIN result : $result")
                    _eventsStateFlow.value = result!!
                } else {
                    Log.e("VM MAIN", "Else de isSuccesful : ${response.errorBody()?.string()}")
                }
            } catch(e: Exception) {
                Log.e("VM MAIN", "Erreur Catch: ${e.message}")
            }
            _isLoading.value = false
        }
    }

    fun navigateToDetails(eventId: Int) {
        viewModelScope.launch {
            _mainSharedFlow.emit(Screen.Details.route + "/$eventId")
        }

    }

//    fun getEventTypes() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            val categories = withContext(Dispatchers.IO) {
//                api.getApi().getEventTypes()
//            }
//            _categories.value = categories
//            _isLoading.value = false
//        }
//    }
}
sealed class SessionState {
    data object Idle : SessionState()
    data object Checking: SessionState()
    data object Unauthorized : SessionState()
    data object Authorized: SessionState()
}
