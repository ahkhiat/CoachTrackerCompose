package com.devid_academy.coachtrackercompose.data.dto
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamDTO (
    val id: Int?,
    val name: String,
    val players: List<PlayerDTO>?,
    val coaches: List<CoachDTO>?
): Parcelable