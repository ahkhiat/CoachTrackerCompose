package com.devid_academy.coachtrackercompose.data.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateEventDTO(

    val date: String,

    @Json(name = "event_type")
    val eventType: Int,

    val team: Int,

    @Json(name = "visitor_team")
    val visitorTeam: Int?,

    val stadium: Int,
    val season: Int,

) : Parcelable