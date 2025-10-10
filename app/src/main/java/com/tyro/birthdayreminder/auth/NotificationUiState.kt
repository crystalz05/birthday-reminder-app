package com.tyro.birthdayreminder.auth

import com.tyro.birthdayreminder.entity.Notification

data class NotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val error: String? = null
)