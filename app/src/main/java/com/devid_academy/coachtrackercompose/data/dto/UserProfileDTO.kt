package com.devid_academy.coachtrackercompose.data.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfileDTO (

    val id: Int,
    val email: String,
    val firstname: String,
    val lastname: String,
    val birthdate: String,
    val phone: String?,
    val roles: List<String>?,

    @Json(name= "plays_in")
    val playsIn: UserTeamDTO?,

    @Json(name= "is_coach_of")
    val isCoachOf: UserTeamDTO?,

    @Json(name= "is_parent_of")
    val isParentOf: List<IsParentOfDTO>?,

    ): Parcelable