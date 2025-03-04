package com.devid_academy.coachtrackercompose.data.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerDTO (

    val id: Int?,
    val user : UserDTO,

    @Json(name = "plays_in")
    val playsInTeam : PlayerDTO?

) : Parcelable