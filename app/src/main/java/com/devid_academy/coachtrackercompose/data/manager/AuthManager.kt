package com.devid_academy.coachtrackercompose.data.manager

import android.util.Log
import com.auth0.android.jwt.JWT

import java.util.Date
import javax.inject.Inject


class AuthManager @Inject constructor(
    private val preferencesManager: PreferencesManager
) {

    fun logout() {
        with(preferencesManager){
            removeToken()
            clearUser()
            clearAll()
        }
    }

    fun isTokenValid(token: String?): Boolean {
        if (token.isNullOrEmpty()) return false

        return try {
            val jwt = JWT(token)
            val expirationTime = jwt.expiresAt

            if (expirationTime != null) {
                val isValid = expirationTime.after(Date())
                Log.d("TOKEN_VALIDATION", "Expiration : $expirationTime | Now : ${Date()} | Valid : $isValid")
                isValid
            } else {
                Log.e("TOKEN_VALIDATION", "Le token ne contient pas de date d'expiration.")
                false
            }
        } catch (e: Exception) {
            Log.e("TOKEN_VALIDATION", "Erreur lors de la validation du token", e)
            false
        }
    }








}