package com.devid_academy.coachtrackercompose.data.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class GoalDTO(

    val id: Int,

    @Json(name = "minute_goal")
    val minuteGoal: Int,

    val player: PlayerDTO,

    val event: EventLiteDTO
): Parcelable


@Parcelize
data class EventLiteDTO (
    val id: Int
): Parcelable
