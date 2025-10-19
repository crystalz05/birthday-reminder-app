package com.tyro.birthdayreminder.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyro.birthdayreminder.data_store.AppSettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val settingsDataStore: AppSettingsDataStore
) : ViewModel() {

    private val _isOnboardingCompleted = MutableStateFlow(false)
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted

    init {
        viewModelScope.launch {
            settingsDataStore.isOnboardingCompleted.collect {
                _isOnboardingCompleted.value = it
            }
        }
    }

    fun setOnboardingCompleted(value: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setOnboardingCompleted(value)
        }
    }

}