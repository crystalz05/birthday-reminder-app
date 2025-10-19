package com.tyro.birthdayreminder.custom_class

import android.app.Activity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

fun setStatusBarIconColor(activity: Activity, darkIcons: Boolean) {
    val window = activity.window
    WindowCompat.setDecorFitsSystemWindows(window, false)

    val insetsController = WindowInsetsControllerCompat(window, window.decorView)
    insetsController.isAppearanceLightStatusBars = darkIcons
}