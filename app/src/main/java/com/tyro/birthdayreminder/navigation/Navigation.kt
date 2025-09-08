package com.tyro.birthdayreminder.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tyro.birthdayreminder.auth.AuthState
import com.tyro.birthdayreminder.ui.screen.AccountEmailVerificationScreen
import com.tyro.birthdayreminder.ui.screen.AccountVerificationScreen
import com.tyro.birthdayreminder.ui.screen.AddBirthdayScreen
import com.tyro.birthdayreminder.ui.screen.BirthdayDetailScreen
import com.tyro.birthdayreminder.ui.screen.EditBirthdayScreen
import com.tyro.birthdayreminder.ui.screen.HomeScreen
import com.tyro.birthdayreminder.ui.screen.LoginScreen
import com.tyro.birthdayreminder.ui.screen.NotificationScreen
import com.tyro.birthdayreminder.ui.screen.ProfileEditScreen
import com.tyro.birthdayreminder.ui.screen.ProfileScreen
import com.tyro.birthdayreminder.ui.screen.ProfileSettingScreen
import com.tyro.birthdayreminder.ui.screen.SignupScreen
import com.tyro.birthdayreminder.ui.screen.SplashScreen
import com.tyro.birthdayreminder.ui.screen.StatsScreen
import com.tyro.birthdayreminder.view_model.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    navHostController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel
) {

    NavHost(
        navController = navHostController,
        startDestination =Screen.Splash.route,
        enterTransition ={ fadeIn(animationSpec = tween(100)) },
        exitTransition = { fadeOut(animationSpec = tween(100)) },
        popEnterTransition = { fadeIn(animationSpec = tween(100)) },
        popExitTransition = { fadeOut(animationSpec = tween(100)) },
    ){
        composable(Screen.Splash.route){
            SplashScreen(authViewModel){ route ->
                navHostController.navigate(route){
                    popUpTo(navHostController.graph.id) {inclusive = true}
                    launchSingleTop = true
                }
            }
        }
        composable(Screen.Home.route){
            HomeScreen(navHostController)
        }
        composable(Screen.AddBirthDay.route) {
            AddBirthdayScreen(navHostController)
        }
        composable(Screen.ProfileSetting.route){
            ProfileSettingScreen(navHostController)
        }
        composable(Screen.BirthDayDetail.route){
            BirthdayDetailScreen(navHostController)
        }
        composable(Screen.ProfileSetting.route){
            ProfileSettingScreen(navHostController)
        }
        composable(Screen.ProfileEdit.route){
            ProfileEditScreen(navHostController)
        }
        composable(Screen.AccountVerification.route){
            AccountVerificationScreen(navHostController)
        }
        composable(Screen.Login.route){
            LoginScreen(navHostController, authViewModel)
        }
        composable(Screen.Stats.route){
            StatsScreen(navHostController)
        }
        composable(Screen.Signup.route){
            SignupScreen(navHostController)
        }
        composable(Screen.EditBirthDay.route){
            EditBirthdayScreen(navHostController)
        }
        composable(Screen.Notification.route) {
            NotificationScreen(navHostController)
        }
        composable(Screen.Profile.route){
            ProfileScreen(navHostController)
        }
        composable(Screen.EmailVerification.route){
            AccountEmailVerificationScreen(navHostController)
        }
    }

}