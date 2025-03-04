package com.devid_academy.coachtrackercompose.data.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class PresenceDTO (

    val id: Int?,

    @Json(name = "on_time")
    val onTime : Boolean,

    val player : PlayerDTO,
    val event: EventDTO?

) : Parcelable