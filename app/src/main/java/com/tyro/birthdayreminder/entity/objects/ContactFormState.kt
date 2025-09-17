package com.tyro.birthdayreminder.entity.objects

import android.graphics.Bitmap

data class ContactFormState(
    val fullName: String = "",               // not null
    val photo: ContactPhoto? = null,           // URL
    val birthday: String = "",                // "YYYY-MM-DD" (Supabase/Postgres `date`)
    val relationship: String = "",
    val personalNote: String = "",
    val phoneNumber: String = "",            // not null
    val email: String = "",
    val instagram: String = "",
    val twitter: String = "",
    val reminders: List<String> = emptyList(), // maps to text[]

    val phoneNumberError: String? = null,
    val fullNameError: String? = null,
    val birthDateError: String? = null,
    val emailError: String? = null
)

sealed class ContactPhoto {
    data class Local(val bitmap: Bitmap) : ContactPhoto()
    data class Remote(val url: String) : ContactPhoto()
}