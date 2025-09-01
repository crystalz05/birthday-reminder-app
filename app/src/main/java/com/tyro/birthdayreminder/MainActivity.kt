package com.tyro.birthdayreminder

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.tyro.birthdayreminder.navigation.Navigation
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

class MainActivity : ComponentActivity() {

    private val authViewModel:AuthViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BirthdayReminderTheme {
//                HomeScreen()
//                LoginScreen()
//                SignupScreen()
//                BirthdayDetailScreen()
//                AddBirthdayScreen()
//                EditBirthdayScreen()
//                StatsScreen()
//                ProfileSettingScreen(navHostController = rememberNavController())
//                AccountVerificationScreen()
//                ProfileEditScreen()
                Navigation(authViewModel = authViewModel)
//                NotificationScreen(navHostController = rememberNavController())
//                CalendarScreen(navHostController = rememberNavController())
//                ContactScreen()
            }
        }
    }
}
