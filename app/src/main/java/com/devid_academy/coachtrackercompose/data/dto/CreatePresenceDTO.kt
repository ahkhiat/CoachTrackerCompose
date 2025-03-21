package com.devid_academy.coachtrackercompose.data.dto

data class CreatePresenceDTO(
    val eventId: Int,
    val playerIds: List<Int>
)