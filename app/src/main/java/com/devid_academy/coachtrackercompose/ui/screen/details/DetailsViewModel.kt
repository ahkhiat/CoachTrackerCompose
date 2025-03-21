package com.devid_academy.coachtrackercompose.ui.screen.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.data.dto.ConvocationDTO
import com.devid_academy.coachtrackercompose.data.dto.EventDTO
import com.devid_academy.coachtrackercompose.data.dto.PresenceDTO
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

    private val _showButtonCreateConvocationsStateFlow = MutableStateFlow<Boolean>(false)
    val showButtonCreateConvocationsStateFlow: StateFlow<Boolean> = _showButtonCreateConvocationsStateFlow

    private val _convocationsListStateFlow = MutableStateFlow<List<ConvocationDTO?>>(emptyList())
    val convocationsListStateFlow: StateFlow<List<ConvocationDTO?>> = _convocationsListStateFlow

    private val _showButtonCreatePresencesStateFlow = MutableStateFlow(false)
    val showButtonCreatePresencesStateFlow: StateFlow<Boolean> = _showButtonCreatePresencesStateFlow

    private val _presencesListStateFlow = MutableStateFlow<List<PresenceDTO?>>(emptyList())
    val presencesListStateFlow: StateFlow<List<PresenceDTO?>> = _presencesListStateFlow

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

                    result?.convocations?.let {
                        _showButtonCreateConvocationsStateFlow.value = it.isEmpty()
                        _convocationsListStateFlow.value = it
                        Log.i("VM DETAILS", "Liste convocations : ${convocationsListStateFlow.value}")
                    }
                    result?.presences?.let {
                        _showButtonCreatePresencesStateFlow.value = it.isEmpty()
                        _presencesListStateFlow.value = it
                        Log.i("VM DETAILS", "Liste presences : ${presencesListStateFlow.value}")
                    }


                } else when (response.code()){
                    500 -> Log.e("VM DETAILS", "VM DETAILS erreur 500")
                }
            } catch(e: Exception) {
                Log.e("VM DETAILS", "Erreur Catch: ${e.message}")
            }
        }
    }

}