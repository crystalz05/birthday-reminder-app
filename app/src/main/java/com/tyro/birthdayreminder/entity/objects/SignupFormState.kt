package com.tyro.birthdayreminder.entity.objects

data class SignupFormState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordMismatch: String? = null,
    val termsCheck: Boolean = false
)
