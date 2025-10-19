package com.tyro.birthdayreminder.ui.screen

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.BorderStroke
import com.tyro.birthdayreminder.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tyro.birthdayreminder.auth.UiEvent
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileAboutSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileAppearanceSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileNotificationSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfilePhotoSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfilePrivacyAndSecuritySection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileSupportSection
import com.tyro.birthdayreminder.view_model.AuthViewModel
import com.tyro.birthdayreminder.view_model.BirthdayContactViewModel
import com.tyro.birthdayreminder.view_model.LoginFormViewModel
import kotlinx.coroutines.launch
import kotlin.math.log


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel,
    loginFormViewModel: LoginFormViewModel = hiltViewModel(),
    birthdayContactViewModel: BirthdayContactViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val formState by loginFormViewModel.formState.collectAsState()
    val email by authViewModel.email.collectAsState()

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        authViewModel.deleteAccount(formState.password){
                            birthdayContactViewModel.deleteAllContacts()
                        }
                    }) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                    }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Delete account", color = MaterialTheme.colorScheme.error) },
                text = {
                    Column {
                        TextField(value = formState.password,
                            onValueChange = { loginFormViewModel.onPasswordChange(it) },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Enter your password to confirm account deletion",  color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
        Box(modifier = Modifier.fillMaxSize()){
            Column {
                ProfilePhotoSection(navHostController, authViewModel)
            }
            OutlinedButton(
                onClick = {
                    email?.let { loginFormViewModel.onEmailChange(it) }
                    Log.d("Profile Screen", formState.email)
                    showDialog = true
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colorResource(id = R.color.orange)
                ),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.error)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.width(8.dp))
                Text("Delete Account", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.error)
            }

        }
    }
}