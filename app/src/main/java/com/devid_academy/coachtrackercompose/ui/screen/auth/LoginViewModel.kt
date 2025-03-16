package com.devid_academy.coachtrackercompose.ui.screen.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.coachtrackercompose.R
import com.devid_academy.coachtrackercompose.data.dto.auth.LoginDTO
import com.devid_academy.coachtrackercompose.data.manager.PreferencesManager
import com.devid_academy.coachtrackercompose.data.network.ApiService
import com.devid_academy.coachtrackercompose.util.ViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {


    private val _loginSharedFlow = MutableSharedFlow<ViewModelEvent?>()
    val loginSharedFlow: SharedFlow<ViewModelEvent?> = _loginSharedFlow

    fun verifyLogin(email: String, password: String) {
        viewModelScope.launch {
            if (email.isNotEmpty() && password.isNotEmpty()) {
                try {
                    val response = withContext(Dispatchers.IO) {
                        api.getApi().loginUser(LoginDTO(email.trim(), password.trim()))
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
                            _loginSharedFlow.emit(ViewModelEvent.NavigateToMainScreen)
                        }
                    } else when (response.code()) {
                        401 -> {
                            Log.d("RESULT CODE 401", "RESULT CODE 401")
                            _loginSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.invalid_credentials))
                        }

                    }
                } catch (e: Exception) {
                    Log.e("Error LoginVM", "Erreur Login VM : ${e.message}")
                }
            } else {
                _loginSharedFlow.emit(ViewModelEvent.ShowSnackBar(R.string.fill_all_inputs))
            }
        }
    }
}


