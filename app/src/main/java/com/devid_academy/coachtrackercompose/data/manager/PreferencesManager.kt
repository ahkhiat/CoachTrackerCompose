package com.devid_academy.coachtrackercompose.data.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.auth0.android.jwt.DecodeException
import com.auth0.android.jwt.JWT
import com.devid_academy.coachtrackercompose.data.dto.UserProfileDTO
import com.devid_academy.coachtrackercompose.util.SHARED_PREFS
import com.devid_academy.coachtrackercompose.util.TEAM_ID
import com.devid_academy.coachtrackercompose.util.TOKEN
import com.devid_academy.coachtrackercompose.util.USER_ID
import com.devid_academy.coachtrackercompose.util.USER_KEY
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Inject


class PreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val adapter: JsonAdapter<UserProfileDTO>
) {

    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN, null)
    }
    fun setToken(token: String) {
        sharedPreferences.edit().putString(TOKEN, token).apply()
    }
    fun removeToken() {
        sharedPreferences.edit().remove(TOKEN).apply()
    }
    fun getTeamId(): Int {
        return sharedPreferences.getInt(TEAM_ID, -1)
    }
    fun setTeamId(id: Int) {
        sharedPreferences.edit().putInt(TEAM_ID, id).apply()
        Log.i("TEAM ID","TEAM ID : ${getTeamId()}" )
    }

    fun saveUser(user: UserProfileDTO) {
        val userJson = adapter.toJson(user)
        sharedPreferences.edit().putString(USER_KEY, userJson).apply()
        Log.i("PREF", "User saved : $userJson")
    }

    fun getUser(): UserProfileDTO? {
        val userJson = sharedPreferences.getString(USER_KEY, null) ?: return null
        Log.i("PREFS", "User de getUser : $userJson")
        return adapter.fromJson(userJson)
    }

    fun clearUser() {
        sharedPreferences.edit().remove(USER_KEY).apply()
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
        Log.i("LOGOUT", "Données utilisateur supprimées")
    }

    fun getUsernameFromToken(): String? {
        val token = sharedPreferences.getString(TOKEN, null)
        if (token.isNullOrEmpty()) {
            return null
        }

        return try {
            val jwt = JWT(token)
            jwt.getClaim("username").asString()
        } catch (exception: DecodeException) {
            null
        }
    }

}