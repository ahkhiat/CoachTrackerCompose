package com.devid_academy.coachtrackercompose.ui.screen.convocation

import androidx.lifecycle.ViewModel
import com.devid_academy.coachtrackercompose.data.manager.PreferencesManager
import com.devid_academy.coachtrackercompose.data.network.ApiService
import javax.inject.Inject

class CreateConvocationViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {

}