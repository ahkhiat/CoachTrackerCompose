package com.devid_academy.coachtrackercompose.data.manager

import android.util.Log
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

import java.util.Date
import javax.inject.Inject


class AuthManager @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    private val _authEvents = MutableSharedFlow<AuthEvent>(replay = 0)
    val authEvents = _authEvents.asSharedFlow()

    fun logout() {
        with(preferencesManager){
            removeToken()
            clearUser()
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

                if (!isValid) {
                    Log.e("TOKEN_VALIDATION", "Token expiré, déclenchement de la déconnexion.")

                }

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

sealed class AuthEvent {
    object Logout : AuthEvent()
}