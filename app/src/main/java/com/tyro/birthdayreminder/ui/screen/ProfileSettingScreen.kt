package com.tyro.birthdayreminder.ui.screen

import android.util.Log
import com.tyro.birthdayreminder.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tyro.birthdayreminder.auth.AuthState
import com.tyro.birthdayreminder.auth.UiEvent
import com.tyro.birthdayreminder.custom_class.Loading
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileAboutSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileAppearanceSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileNotificationSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfilePhotoSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfilePhotoSection_2
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfilePrivacyAndSecuritySection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileSupportSection
import com.tyro.birthdayreminder.view_model.AuthViewModel
import com.tyro.birthdayreminder.view_model.ThemeViewModel
import io.ktor.util.collections.getValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }

    val authState by authViewModel.authState.collectAsState()

    val haptic = LocalHapticFeedback.current

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        authViewModel.uiEvent.collect{ event ->
            when(event){
                is UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                is UiEvent.Navigate -> navHostController.navigate(event.route)
                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                TopAppBar(
                    title = {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("Profile Settings", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleLarge)

                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {navHostController.navigateUp()},
                            modifier = Modifier.padding(start = 10.dp, end = 20.dp)
                        ){
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    actions = {
                        Icon(imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                        Spacer(Modifier.width(8.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        }


    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)){
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    authViewModel.signOut(context)
                                    showDialog = false
                                }) {
                                    Text("Sign Out", color = MaterialTheme.colorScheme.error)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    showDialog = false
                                }) {
                                    Text("Cancel")
                                }
                            },
                            title = { Text("Confirm Sign Out") },
                            text = { Text("Are you sure you want to sign out?") }
                        )
                    }

                    ProfilePhotoSection_2(navHostController, authViewModel)
                    ProfileNotificationSection()
                    ProfileAppearanceSection(themeViewModel)
                    ProfilePrivacyAndSecuritySection()
                    ProfileSupportSection()
                    ProfileAboutSection()
                    Spacer(Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = {
                            showDialog = true
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colorResource(id = R.color.orange)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ExitToApp,
                            contentDescription = null,
                            tint = colorResource(id = R.color.orange)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Sign Out", style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }


            if (authState is AuthState.Loading) {
                Loading()
            }
        }


    }
}