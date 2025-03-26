package com.devid_academy.coachtrackercompose.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateGoalDTO(

    val eventId: Int,
    val playerId: Int,
    val minuteGoal: Int

): Parcelable {

}
