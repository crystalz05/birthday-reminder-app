package com.tyro.birthdayreminder.entity

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val id: String? = null,              // maps to uuid
    val userId: String,                 // Firebase UID
    val fullName: String,               // not null
    val photo: String? = null,           // URL
    val birthday: String,                // "YYYY-MM-DD" (Supabase/Postgres `date`)
    val relationship: String? = null,
    val personalNote: String? = null,
    val phoneNumber: String,            // not null
    val email: String? = null,
    val instagram: String? = null,
    val twitter: String? = null,
    val reminders: List<String> = emptyList(), // maps to text[]
    val fcmToken: String? = null,
    val createdAt: String? = null,       // ISO timestamp from Supabase
    val updatedAt: String? = null
)

//Instead of a bunch of booleans (like twoWeeksBefore = true), using a list of codes ("2w", "1w", "3d", "0d") is cleaner:
//"2w" → 2 weeks before
//"1w" → 1 week before
//"3d" → 3 days before
//"0d" → on the day
//That way, you can easily loop in your cron function:

//for (const reminder of contact.reminders) {
//    if (shouldTriggerReminder(today, contact.birthday, reminder)) {
//        sendNotification(contact)
//    }
//}