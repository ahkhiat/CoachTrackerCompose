package com.devid_academy.coachtrackercompose.data.network

import android.util.Log
import com.devid_academy.coachtrackercompose.data.manager.AuthManager
import com.devid_academy.coachtrackercompose.data.manager.PreferencesManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val authManager: AuthManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var token = preferencesManager.getToken()

        if (!authManager.isTokenValid(token)) {
            Log.e("AuthInterceptor", "Token expiré ou invalide, suppression.")
            preferencesManager.removeToken()
            token = null
        }

        val originalRequest: Request = chain.request()

        val newRequest = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }
        return chain.proceed(newRequest)
    }
}