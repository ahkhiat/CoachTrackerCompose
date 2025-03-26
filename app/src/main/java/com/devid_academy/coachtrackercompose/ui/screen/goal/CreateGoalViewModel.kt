package com.devid_academy.coachtrackercompose.ui.screen.goal

import androidx.lifecycle.ViewModel
import com.devid_academy.coachtrackercompose.data.manager.PreferencesManager
import com.devid_academy.coachtrackercompose.data.network.ApiService
import javax.inject.Inject

class CreateGoalViewModel @Inject constructor(
    private val api: ApiService,
    private val pm: PreferencesManager
): ViewModel() {


}