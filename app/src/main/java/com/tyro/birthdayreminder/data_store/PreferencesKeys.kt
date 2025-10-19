package com.tyro.birthdayreminder.data_store

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val THEME = stringPreferencesKey("theme-mode")
    val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    val ALARM_ENABLED = booleanPreferencesKey("alarm_enabled")
}