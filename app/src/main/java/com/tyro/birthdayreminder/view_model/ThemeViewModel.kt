package com.tyro.birthdayreminder.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tyro.birthdayreminder.custom_class.ThemeMode
import com.tyro.birthdayreminder.data_store.AppSettingsDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application): AndroidViewModel(application) {

    private val themeDataStore = AppSettingsDataStore(application)

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode


    init{
        viewModelScope.launch {
            themeDataStore.themeModeFlow.collect{
                savedMode ->
                _themeMode.value = when (savedMode){
                    "LIGHT" -> ThemeMode.LIGHT
                    "DARK" -> ThemeMode.DARK
                    else -> ThemeMode.SYSTEM
                }
            }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            themeDataStore.setThemeMode(mode.name)
        }
    }

    fun toggleDarkLight() {
        val newMode = when (_themeMode.value) {
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.DARK -> ThemeMode.LIGHT
            ThemeMode.SYSTEM -> ThemeMode.LIGHT
        }
        setThemeMode(newMode)
    }

    fun useSystemTheme() {
        setThemeMode(ThemeMode.SYSTEM)
    }

}