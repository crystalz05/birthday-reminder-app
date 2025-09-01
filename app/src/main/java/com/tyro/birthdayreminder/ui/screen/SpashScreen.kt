package com.tyro.birthdayreminder.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.auth.AuthState
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.view_model.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    authViewModel: AuthViewModel,
    onSplashFinished:(String) -> Unit
){

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        delay(300L)
        when(authState){
             AuthState.Verified -> {
                onSplashFinished(Screen.Home.route)
            }
            AuthState.Loading -> TODO()
            AuthState.LoggedOut -> TODO()
            AuthState.Unverified -> TODO()
        }

    }

    Box(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.surface),
        Alignment.Center)
    {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Splash icon")
            Spacer(modifier = Modifier.height(25.dp))
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
        }
    }
}