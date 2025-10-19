package com.tyro.birthdayreminder.data_store

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

// ✅ Single source of truth for DataStore instance
val Context.dataStore by preferencesDataStore(name = "settings")
