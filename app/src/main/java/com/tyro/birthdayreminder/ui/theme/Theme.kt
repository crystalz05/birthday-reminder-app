package com.tyro.birthdayreminder.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = ColorLightTokens.Primary,
    onPrimary = ColorLightTokens.OnPrimary,
    secondary = ColorLightTokens.Secondary,
    onSecondary = ColorLightTokens.OnSecondary,
    background = ColorLightTokens.Background,
    onBackground = ColorLightTokens.OnBackground,
    surface = ColorLightTokens.Surface,
    onSurface = ColorLightTokens.OnSurface,
    error = ColorLightTokens.Error,
    onError = ColorLightTokens.OnError,
    primaryContainer = ColorLightTokens.PrimaryContainer,
    tertiary = ColorLightTokens.Tertiary,
    surfaceVariant = ColorLightTokens.SurfaceVariant,
)

private val DarkColorScheme = darkColorScheme(
    primary = ColorDarkTokens.Primary,
    onPrimary = ColorDarkTokens.OnPrimary,
    secondary = ColorDarkTokens.Secondary,
    onSecondary = ColorDarkTokens.OnSecondary,
    background = ColorDarkTokens.Background,
    onBackground = ColorDarkTokens.OnBackground,
    surface = ColorDarkTokens.Surface,
    onSurface = ColorDarkTokens.OnSurface,
    error = ColorDarkTokens.Error,
    onError = ColorDarkTokens.OnError,
    primaryContainer = ColorDarkTokens.PrimaryContainer,
    tertiary = ColorDarkTokens.Tertiary,
    surfaceVariant = ColorDarkTokens.SurfaceVariant,
)


@Composable
fun BirthdayReminderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}