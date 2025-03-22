package com.devid_academy.coachtrackercompose.ui.screen.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.R
import com.devid_academy.coachtrackercompose.data.dto.auth.RegisterDTO
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
class RegisterViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _registerSharedFlow = MutableSharedFlow<ViewModelEvent?>()
    val registerSharedFlow: SharedFlow<ViewModelEvent?> = _registerSharedFlow

//    private var result : Result<StatusAuthDTO>? = null

    fun register(email: String, password: String,
                 passwordConfirm: String, firstname: String,
                 lastname: String, birthdate: String) {
        viewModelScope.launch {
            _isLoading.value = true
            if(email.isNotEmpty() && password.isNotEmpty()
                && passwordConfirm.isNotEmpty() && firstname.isNotEmpty()
                && lastname.isNotEmpty() && birthdate.isNotEmpty()) {

                if(password == passwordConfirm) {
                    val response = withContext(Dispatchers.IO) {
                        api.getApi().registerUser(
                            RegisterDTO(email, password, firstname,
                                lastname, birthdate)
                        )
                    }
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result?.token != null) {
                            pm.setToken(result.token)
                            val userProfile = withContext(Dispatchers.IO) {
                                api.getApi().getUserProfile()
                            }
                            Log.i("USER PROFILE", "User profile : ${userProfile}")
                            pm.saveUser(userProfile)
                            Log.i("SAVED USER", "User saved in preferences: ${pm.getUser()}")
                            val teamId = userProfile.isCoachOf?.id
                                ?: userProfile.playsIn?.id
                                ?: userProfile.isParentOf?.firstOrNull()?.playsIn?.id
                            Log.i("TEAM ID", "TEAM ID : $teamId")
                            teamId?.let {
                                pm.setTeamId(it)
                            }
                            _isLoading.value = false
                            _registerSharedFlow.emit(ViewModelEvent.NavigateToMainScreen)
                        }

                    } else when (response.code()) {
                        500 -> {
                            Log.d("RESULT CODE 500", "RESULT CODE 500")
                            _isLoading.value = false
                            _registerSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.server_error))                        }
                        400 -> {
                            Log.d("RESULT CODE 400", "RESULT CODE 400")
                            _isLoading.value = false
                            _registerSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.fill_all_inputs))                        }
                    }
                } else {
                    _isLoading.value = false
                    _registerSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.passwords_different))
                }
            } else {
                _isLoading.value = false
                _registerSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.fill_all_inputs))
            }
        }

    }
}


