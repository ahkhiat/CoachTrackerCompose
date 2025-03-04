package com.devid_academy.coachtrackercompose.data.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class IsParentOfDTO (

    val id: Int,
    val firstname: String,
    val lastname: String,

    @Json(name = "plays_in")
    val playsIn: TeamDTO?

): Parcelable
