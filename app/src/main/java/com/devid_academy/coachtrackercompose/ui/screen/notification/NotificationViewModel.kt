package com.devid_academy.coachtrackercompose.ui.screen.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.data.dto.EventDTO
import com.devid_academy.coachtrackercompose.data.manager.PreferencesManager
import com.devid_academy.coachtrackercompose.data.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {

    private val _counterEventWithoutConvocation = MutableStateFlow<Int>(0)
    val counterEventWithoutConvocation: StateFlow<Int> = _counterEventWithoutConvocation

    private val _eventWithoutConvocList = MutableStateFlow<List<EventDTO>>(emptyList())
    val eventWithoutConvocList: StateFlow<List<EventDTO>> = _eventWithoutConvocList

    init {
        getEvents()
    }

    fun getEvents() {
        viewModelScope.launch {
            try {
                val user = pm.getUser()
                val teamName = user?.isCoachOf?.name
                    ?: user?.playsIn?.name
                    ?: user?.isParentOf?.firstOrNull()?.playsIn?.name
                Log.i("VM MAIN", "Team Name : $teamName")
                val response = withContext(Dispatchers.IO) {
                    api.getApi().getAllEvents(teamName!!)
                }
                if(response.isSuccessful) {
                    val result = response.body()
                    Log.i("VM NOTIFS", "VM NOTIFS result : $result")
                    result?.let {
                        for (event in result) {
                            if(event.hasConvocations == false) {
                                Log.i("DEBUG", "Ajout à la liste : ${event.id}")
                                _counterEventWithoutConvocation.value = (_counterEventWithoutConvocation.value ?: 0) + 1
                                _eventWithoutConvocList.value = _eventWithoutConvocList.value
                                    .toMutableList().apply {
                                        add(event)
                                    }
                            }
                        }
                    }
                    Log.i("VM NOTIFS", "NB EVENT WITHOUT CONVOC : ${_counterEventWithoutConvocation.value}")
                    Log.i("VM NOTIFS", "LIST EVENT WITHOUT CONVOC : ${eventWithoutConvocList.value}")
                } else {
                    Log.e("VM NOTIFS", "Else de isSuccesful : ${response.errorBody()?.string()}")
                }
            } catch(e: Exception) {
                Log.e("VM NOTIFS", "Erreur Catch: ${e.message}")
            }
        }
    }

}