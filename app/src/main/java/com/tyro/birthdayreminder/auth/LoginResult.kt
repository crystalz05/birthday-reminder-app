package com.tyro.birthdayreminder.auth

import com.google.firebase.auth.FirebaseUser

sealed class LoginResult {
    data class Verified(val user: FirebaseUser) : LoginResult()
    data class Unverified(val user: FirebaseUser) : LoginResult()
    data class Error(val message: String) : LoginResult()
}