package com.tyro.birthdayreminder.entity

import kotlinx.serialization.Serializable

//data class Notification(
//    val title: String = "",
//    val body: String = "",
//    val timeStamp: String = "",
//)
//
@Serializable
data class Notification(
    val id: String,
    val user_id: String,
    val contact_id: String?,
    val title: String,
    val body: String,
    val sent_at: String
)