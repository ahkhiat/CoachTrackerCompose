package com.devid_academy.coachtrackercompose.data.dto

import android.os.Parcelable
import android.provider.Settings.Global.getString
import com.devid_academy.coachtrackercompose.R
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerProfileDTO (

    val id: Int = 0,
    val email: String = "not provided",
    val firstname: String = "not provided",
    val lastname: String = "not provided",
    val birthdate: String = "not provided",
    val phone: String? = "not provided",
    val roles: List<String>? = emptyList(),

    @Json(name= "plays_in")
    val playsIn: UserTeamDTO? = null,

    @Json(name= "is_coach_of")
    val isCoachOf: UserTeamDTO? = null,

    @Json(name= "is_parent_of")
    val isParentOf: List<IsParentOfDTO>? = null,

    val stats: StatsDTO? = null
    ): Parcelable

@Parcelize
data class StatsDTO(
    val convocations: Int,
    val presences: Int,
    val goals: Int
): Parcelable