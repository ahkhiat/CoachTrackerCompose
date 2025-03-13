package com.devid_academy.coachtrackercompose.data.di

import android.content.Context
import android.content.SharedPreferences
import com.devid_academy.coachtrackercompose.data.dto.UserProfileDTO
import com.devid_academy.coachtrackercompose.data.manager.AuthManager
import com.devid_academy.coachtrackercompose.data.manager.PreferencesManager
import com.devid_academy.coachtrackercompose.data.network.ApiInterface
import com.devid_academy.coachtrackercompose.data.network.ApiRoutes
import com.devid_academy.coachtrackercompose.data.network.AuthInterceptor
import com.devid_academy.coachtrackercompose.util.SHARED_PREFS
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context):
            SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    }
    @Provides
    @Singleton
    fun providePreferencesManager(
        sharedPreferences: SharedPreferences,
        adapter: JsonAdapter<UserProfileDTO>
    ): PreferencesManager {
        return PreferencesManager(sharedPreferences, adapter)
    }
    @Provides
    @Singleton
    fun provideAuthManager(preferencesManager: PreferencesManager) : AuthManager {
        return AuthManager(preferencesManager)
    }
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideUserProfileAdapter(moshi: Moshi): JsonAdapter<UserProfileDTO> {
        return moshi.adapter(UserProfileDTO::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(preferencesManager: PreferencesManager, authManager: AuthManager): AuthInterceptor {
        return AuthInterceptor(preferencesManager, authManager)
    }
    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(authInterceptor)
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiRoutes.BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                Moshi.Builder().add(
                    KotlinJsonAdapterFactory()
                ).build()))
            .client(okHttpClient)
            .build()
    }
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }


}