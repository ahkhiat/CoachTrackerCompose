package com.devid_academy.coachtrackercompose.data.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateEventDTO(

    val date: String,
    val eventTypeId: Int,
    val teamId: Int,
    val visitorTeamId: Int?,
    val stadiumId: Int,
    val seasonId: Int,

) : Parcelable