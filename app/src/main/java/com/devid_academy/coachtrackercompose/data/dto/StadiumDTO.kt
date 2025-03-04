package com.devid_academy.coachtrackercompose.data.dto
import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class StadiumDTO(
    val id: Int?,
    val name: String,
    val adress: String?,

    @Json(name = "postal_code")
    val postalCode: String?,

    val town: String?
    
): Parcelable {
//    override fun toString(): String {
//        return name
//    }
}