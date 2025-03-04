package com.devid_academy.coachtrackercompose.data.di

import android.content.Context
import android.content.SharedPreferences
import com.devid_academy.coachtrackercompose.data.dto.UserProfileDTO
import com.devid_academy.coachtrackercompose.data.manager.AuthManager
import com.devid_academy.coachtrackercompose.data.manager.PreferencesManager
import com.devid_academy.coachtrackercompose.util.SHARED_PREFS
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
}