package com.tyro.birthdayreminder.auth

import com.google.firebase.auth.FirebaseUser

sealed class AuthState {
    object Idle: AuthState()
    object Loading : AuthState()
    object LoggedOut : AuthState()
    data class Unverified(val user: FirebaseUser) :  AuthState()
    data class Success(val user: FirebaseUser) :  AuthState()
    data class Verified(val user: FirebaseUser) : AuthState()
    data class Error(val message: String): AuthState()
}