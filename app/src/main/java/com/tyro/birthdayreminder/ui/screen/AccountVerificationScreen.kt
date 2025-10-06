package com.tyro.birthdayreminder.ui.screen

import android.util.Log
import com.tyro.birthdayreminder.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.tyro.birthdayreminder.auth.UiEvent
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileAboutSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileAppearanceSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileNotificationSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfilePhotoSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfilePrivacyAndSecuritySection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileSupportSection
import com.tyro.birthdayreminder.view_model.AuthViewModel
import com.tyro.birthdayreminder.view_model.LoginFormViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountVerificationScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel,
    loginFormViewModel: LoginFormViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current

    val imageUrl by authViewModel.imageUrl.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    val fullName by authViewModel.fullName.collectAsState()
    val email by authViewModel.email.collectAsState()

    val formState by loginFormViewModel.formState.collectAsState()

    LaunchedEffect(Unit){
        email?.let { loginFormViewModel.onEmailChange(it) }
    }

    LaunchedEffect(Unit) {
        launch {
            loginFormViewModel.uiEvent.collect{event ->
                when(event){
                    is UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                    else -> Unit
                }
            }
        }
        launch {
            authViewModel.uiEvent.collect{ event ->
                when(event){
                    is UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                    is UiEvent.Navigate -> navHostController.navigate(event.route)
                    else -> Unit
                }

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
                            Text("Account Verification", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground,
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
                        Icon(imageVector = Icons.Default.Lock,
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

        LazyColumn(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            item {
                Card(modifier = Modifier.shadow(elevation = 2.dp, shape = RoundedCornerShape(6.dp), clip = false)
                    .background(color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(width = 2.dp, color = Color.Transparent, shape = RoundedCornerShape(6.dp))
                    .padding(16.dp),
                    content = {
                        Box(modifier = Modifier.fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(Modifier.size(110.dp, 110.dp).background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f), shape = CircleShape), Alignment.Center){
                                    Box(Modifier.size(100.dp, 100.dp).background(Color.White, shape = CircleShape), Alignment.Center, content = {})
                                    AsyncImage(
                                        model = imageUrl,
                                        contentDescription = "Profile Photo",
                                        placeholder = painterResource(id = R.drawable.baseline_person_24),
                                        error = painterResource(id = R.drawable.baseline_person_24),
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(100.dp)
                                            .align(Alignment.Center)
                                            .clip(CircleShape)
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                fullName?.let {
                                    Text(it,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Normal)
                                }
                                email?.let {
                                    Text(it,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                                        fontWeight = FontWeight.Normal)
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                )
                Spacer(Modifier.height(16.dp))
                Text(modifier = Modifier.fillMaxWidth(), text = "Enter your password to verify it's your account", textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    value = formState.password,
                    visualTransformation = if(formState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    onValueChange = {loginFormViewModel.onPasswordChange(it)},
                    placeholder = { Text("Enter your password")},
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None),
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "") },
                    trailingIcon = {
                        IconButton(onClick = {loginFormViewModel.togglePasswordVisibility()}) {
                            Icon(painter = painterResource(id = if(formState.isPasswordVisible) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24),
                                contentDescription = "")
                        }
                    }
                )
                Spacer(Modifier.height(16.dp))
                Button(onClick = {
                    authViewModel.verifyUserAccount(formState.password)
                    focusManager.clearFocus()
//                    navHostController.navigate(Screen.ProfileEdit.route)
                                 },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Transparent),
                    shape = RoundedCornerShape(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                        Spacer(Modifier.width(8.dp))
                        Text("Proceed", style = MaterialTheme.typography.titleMedium)
                        Icon(imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight, contentDescription = null)
                    }

                }
            }
        }
    }
}