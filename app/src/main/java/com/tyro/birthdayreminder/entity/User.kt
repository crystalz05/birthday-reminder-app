package com.tyro.birthdayreminder.entity

data class User(
    val uid: String = "",
    val fullName: String = "",
    val newUser: Boolean = true,
    val email: String = "",
    val profilePhotoUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)