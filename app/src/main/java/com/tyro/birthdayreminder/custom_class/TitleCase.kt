package com.tyro.birthdayreminder.custom_class

fun TitleCase(text: String): String {
    return text.lowercase().replaceFirstChar {
        if(it.isLowerCase()) it.titlecase() else it.toString()
    }
}