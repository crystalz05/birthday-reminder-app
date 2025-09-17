package com.tyro.birthdayreminder.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.tyro.birthdayreminder.custom_class.connection_manager.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ConnectivityViewModel @Inject constructor(
    private val observer: ConnectivityObserver,
    @ApplicationContext private val context: Context): ViewModel()
{

    private val _status = MutableStateFlow(ConnectivityObserver.Status.Unavailable)
    val status: StateFlow<ConnectivityObserver.Status> = _status

    init {
        viewModelScope.launch {
            observer.observe().collect{
                state -> _status.update { state }
            }
        }
    }
}