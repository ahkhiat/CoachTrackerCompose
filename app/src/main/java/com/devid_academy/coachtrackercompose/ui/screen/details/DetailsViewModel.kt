package com.devid_academy.coachtrackercompose.ui.screen.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.data.dto.EventDTO
import com.devid_academy.coachtrackercompose.data.network.ApiService
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
class DetailsViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {

    private val _eventStateFlow = MutableStateFlow<EventDTO?>(null)
    val eventStateFlow: StateFlow<EventDTO?> = _eventStateFlow

    private val _detailsSharedFlow = MutableSharedFlow<EventDTO?>()
    val detailsSharedFlow: SharedFlow<EventDTO?> = _detailsSharedFlow

    fun getEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.getApi().getEvent(eventId)
                }
                if(response.isSuccessful) {
                    val result = response.body()
                    Log.i("VM DETAILS", "VM DETAILS result : $result")
                    _eventStateFlow.value = result
                } else when (response.code()){
                    500 -> Log.e("VM DETAILS", "VM DETAILS erreur 500")
                }
            } catch(e: Exception) {
                Log.e("VM DETAILS", "Erreur Catch: ${e.message}")
            }
        }
    }

}