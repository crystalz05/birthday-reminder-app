package com.tyro.birthdayreminder.data_store

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppSettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
){

    val themeModeFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[PreferencesKeys.THEME] ?:"SYSTEM" }

    val alarmEnabledFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[PreferencesKeys.ALARM_ENABLED] ?: false }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false }

    suspend fun setThemeMode(mode: String){
        context.dataStore.edit {
            preferences -> preferences[PreferencesKeys.THEME] = mode
        }
    }

    suspend fun setAlarmEnabled(enabled: Boolean){
        context.dataStore.edit {prefs ->
            prefs[PreferencesKeys.ALARM_ENABLED] = enabled
        }
    }

    suspend fun setOnboardingCompleted(enabled: Boolean){
        context.dataStore.edit {prefs ->
            Log.d("App setting data store", "From app setting data store: $enabled")
            Log.d("App setting data store", "From app setting data store again first: ${prefs[PreferencesKeys.ONBOARDING_COMPLETED]}")

            prefs[PreferencesKeys.ONBOARDING_COMPLETED] = enabled
            Log.d("App setting data store", "From app setting data store again: ${prefs[PreferencesKeys.ONBOARDING_COMPLETED]}")

        }
    }
}