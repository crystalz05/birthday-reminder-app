package com.tyro.birthdayreminder.view_model

import androidx.lifecycle.ViewModel
import com.tyro.birthdayreminder.auth.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel(): ViewModel() {

    private val _authState = MutableStateFlow(AuthState.Verified)
    val authState: StateFlow<AuthState> = _authState

}