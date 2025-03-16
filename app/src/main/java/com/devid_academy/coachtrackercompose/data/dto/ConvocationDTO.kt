package com.devid_academy.coachtrackercompose.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//@Parcelize
//data class ConvocationDTO (
//
//    val id: Int?,
//    val player: PlayerDTO,
//    val event: EventDTO?,
//    val status: Int
//
//) : Parcelable

@Parcelize
data class ConvocationDTO (

    val status: Int,
    val player: PlayerDTO,

) : Parcelable