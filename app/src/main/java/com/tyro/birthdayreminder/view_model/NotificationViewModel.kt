package com.tyro.birthdayreminder.view_model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyro.birthdayreminder.auth.NotificationUiState
import com.tyro.birthdayreminder.entity.Notification
import com.tyro.birthdayreminder.repository.AuthRepository
import com.tyro.birthdayreminder.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _notificationUiState = MutableStateFlow(NotificationUiState())
    val notificationUiState: StateFlow<NotificationUiState> = _notificationUiState

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

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
                _notificationUiState.update { it.copy(isLoading = false, error = null) }
                _notifications.update { notifications }
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

                    Log.d("Notification view model", newNotification.toString())
                    _notifications.update {currentState ->  buildList {
                        add(newNotification)
                        addAll(currentState)
                    }
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

    //deleting notification
    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteNotification(notificationId: String){
        viewModelScope.launch {

            _notificationUiState.update { it.copy(isLoading = true) }

            //get result of deleted notification
            val result = notificationRepository.deleteNotification(notificationId)
            Log.d("notification view model", "Before Success ${_notifications.value}")
            //return on successful delete
            result.onSuccess { deleted ->
                Log.d("notification view model", "After success ${_notifications.value}")
                _notificationUiState.update { current -> current.copy(isLoading = false, error = null) }
                _notifications.update { it -> it.filterNot { it.id == deleted.id } }
            }.onFailure { e->
                _notificationUiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Unknown error occurred") }
            }
        }
    }

    fun sendWelcomeNotification(){
        viewModelScope.launch(Dispatchers.IO) {

            val userId = authRepository.getUid() ?: run {
                Log.e("NotificationVM", "User not logged in")
                return@launch
            }

            val snapshot = authRepository.getUserData(userId)
            val isNewUser = snapshot.getBoolean("newUser") ?: false
            if (!isNewUser) {
                Log.d("NotificationVM", "User is not new — skipping notification")
                return@launch
            }

            val result = notificationRepository.sendFcmNotification(userId)

            result.onSuccess {
                Log.d("NotificationVM", "Notification sent successfully")
                authRepository.updateUserData(userId, mapOf("newUser" to false))
            }.onFailure {
                Log.e("NotificationVM", "Failed to send notification")
            }

        }
    }

}