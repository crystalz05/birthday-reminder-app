package com.tyro.birthdayreminder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.LocalUseFallbackRippleImplementation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.tyro.birthdayreminder.custom_class.Loading
import com.tyro.birthdayreminder.custom_class.ThemeMode
import com.tyro.birthdayreminder.navigation.Navigation
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.ui.screen.AccountEmailVerificationScreen
import com.tyro.birthdayreminder.ui.screen.AccountVerificationScreen
import com.tyro.birthdayreminder.ui.screen.AddBirthdayScreen
import com.tyro.birthdayreminder.ui.screen.BirthdayDetailScreen
import com.tyro.birthdayreminder.ui.screen.ContactScreen
import com.tyro.birthdayreminder.ui.screen.EditBirthdayScreen
import com.tyro.birthdayreminder.ui.screen.HomeScreen
import com.tyro.birthdayreminder.ui.screen.LoginScreen
import com.tyro.birthdayreminder.ui.screen.NotificationScreen
import com.tyro.birthdayreminder.ui.screen.ProfileEditScreen
import com.tyro.birthdayreminder.ui.screen.ProfileSettingScreen
import com.tyro.birthdayreminder.ui.screen.SignupScreen
import com.tyro.birthdayreminder.ui.screen.StatsScreen
import com.tyro.birthdayreminder.ui.theme.BirthdayReminderTheme
import com.tyro.birthdayreminder.view_model.AuthViewModel
import com.tyro.birthdayreminder.view_model.BirthdayContactViewModel
import com.tyro.birthdayreminder.view_model.ConnectivityViewModel
import com.tyro.birthdayreminder.view_model.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel:AuthViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels()
    private val birthdayContactViewModel: BirthdayContactViewModel by viewModels()
    private val connectivityViewModel: ConnectivityViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }

        val startDestination = intent?.getStringExtra("route") ?: Screen.Splash.route

        setContent {

            val themeMode by themeViewModel.themeMode.collectAsState()
            val isSystemDark = isSystemInDarkTheme()


            val isDark = when(themeMode){
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM -> isSystemDark
            }

            CompositionLocalProvider() {
                BirthdayReminderTheme(darkTheme = isDark) {
//                HomeScreen()
//                LoginScreen()
//                SignupScreen()
//                BirthdayDetailScreen()
//                AddBirthdayScreen()
//                EditBirthdayScreen()
//                StatsScreen()
//                ProfileSettingScreen(navHostController = rememberNavController())
//                AccountVerificationScreen(
//                    navHostController = rememberNavController()
//                )
//                AccountEmailVerificationScreen(navHostController = rememberNavController())
//                ProfileEditScreen()
                    Navigation(
                        authViewModel = authViewModel,
                        themeViewModel =  themeViewModel,
                        birthdayContactViewModel = birthdayContactViewModel,
                        connectivityViewModel = connectivityViewModel,
                        startDestination = startDestination
                    )
//                NotificationScreen(navHostController = rememberNavController())
//                CalendarScreen(navHostController = rememberNavController())
//                ContactScreen()
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
    }
}

