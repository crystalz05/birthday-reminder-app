package com.tyro.birthdayreminder.data_store

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow


class ThemeDataStore(private val context: Context){
    val themeModeFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[PreferencesKeys.THEME] ?:"SYSTEM" }


    suspend fun setThemeMode(mode: String){
        context.dataStore.edit {
            preferences -> preferences[PreferencesKeys.THEME] = mode
        }
    }
}