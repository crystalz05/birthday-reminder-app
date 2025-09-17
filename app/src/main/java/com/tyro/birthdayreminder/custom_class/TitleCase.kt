package com.tyro.birthdayreminder.custom_class

fun titleCase(text: String): String {
    return text.lowercase().replaceFirstChar {
        if(it.isLowerCase()) it.titlecase() else it.toString()
    }
}