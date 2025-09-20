package com.tyro.birthdayreminder.auth

import com.tyro.birthdayreminder.entity.Contact

sealed class ContactOperationState {
    object Idle : ContactOperationState()
    object Loading : ContactOperationState()
    data class Success<T>(val data: T) : ContactOperationState()
    data class Error(val message: String) : ContactOperationState()
}