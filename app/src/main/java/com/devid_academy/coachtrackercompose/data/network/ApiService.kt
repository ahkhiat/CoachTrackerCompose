package com.devid_academy.coachtrackercompose.data.network

import androidx.lifecycle.ViewModel

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject


class ApiService @Inject constructor(
    private val apiInterface: ApiInterface
) {
    fun getApi(): ApiInterface = apiInterface
}
