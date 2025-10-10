package com.tyro.birthdayreminder.view_model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyro.birthdayreminder.auth.NotificationUiState
import com.tyro.birthdayreminder.auth.UiEvent
import com.tyro.birthdayreminder.entity.Notification
import com.tyro.birthdayreminder.repository.AuthRepository
import com.tyro.birthdayreminder.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _notificationUiState = MutableStateFlow(NotificationUiState())
    val notificationUiState: StateFlow<NotificationUiState> = _notificationUiState

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNotifications(){
        viewModelScope.launch {
            _notificationUiState.update { it.copy(isLoading = true, error = null) }

            val userId = authRepository.getUid()
            if (userId == null) {
                _notificationUiState.update { it.copy(error = "User not logged in", isLoading = false) }
                return@launch
            }

            val result = notificationRepository.getNotifications(userId)

            result.onSuccess { notifications ->
                _notificationUiState.update { it.copy(isLoading = false, notifications = notifications, error = null) }
            }.onFailure { e->
                _notificationUiState.update { it.copy(isLoading = false, error = e.message ?: "Unknown error occurred") }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun observeRealtimeNotifications() {
        viewModelScope.launch {
            val userId = authRepository.getUid() ?: return@launch

            // ✅ pass viewModelScope to keep channel active as long as ViewModel is alive
            notificationRepository.observeNewNotifications(userId, viewModelScope)
                .collect { newNotification ->
                    _notificationUiState.update { currentState ->
                        // Always create a *new* list instance — forces Compose recomposition
                        currentState.copy(
                            notifications = buildList {
                                add(newNotification)
                                addAll(currentState.notifications)
                            }
                        )
                    }
                }
        }
    }

    // ✅ Clean up the Supabase Realtime channel when ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            notificationRepository.clearChannel()
        }
    }

}