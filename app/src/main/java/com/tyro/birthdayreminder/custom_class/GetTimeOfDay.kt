package com.tyro.birthdayreminder.custom_class

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime


@RequiresApi(Build.VERSION_CODES.O)
fun getTimeOfDay(): String {
    val now = LocalTime.now()
    return when(now.hour){
        in 0..11 -> "morning"
        in 11..17 -> "afternoon"
        else -> "evening"
    }
}