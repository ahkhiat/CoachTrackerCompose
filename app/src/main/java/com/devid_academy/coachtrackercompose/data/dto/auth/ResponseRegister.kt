package com.devid_academy.coachtrackercompose.data.dto.auth

import com.squareup.moshi.Json

data class ResponseRegister (

    @Json(name= "token")
    val token: String?,

    @Json (name="errors")
    val errors: List<String>?

)