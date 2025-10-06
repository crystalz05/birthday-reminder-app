package com.tyro.birthdayreminder.entity

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val id: String? = null,
    val userId: String,
    val fullName: String,
    val photo: String? = null,
    val birthday: String,
    val relationship: String? = null,
    val personalNote: String? = null,
    val phoneNumber: String,
    val email: String? = null,
    val instagram: String? = null,
    val twitter: String? = null,
    val reminders: List<String> = emptyList(),
    val fcmToken: String? = null,
    val createdAt: String? = null,

    val journal: String? = null,
    val gender: String? = null,
    val wishList: List<WishItem>? = emptyList(),

    val updatedAt: String? = null
)

@Serializable
data class WishItem(
    val name: String,
    val selected: Boolean = false
)