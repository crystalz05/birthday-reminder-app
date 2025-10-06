package com.tyro.birthdayreminder.ui.screen

import com.tyro.birthdayreminder.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tyro.birthdayreminder.auth.UiEvent
import com.tyro.birthdayreminder.entity.objects.SignupFormState
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileAboutSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileAppearanceSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileNotificationSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfilePhotoSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfilePrivacyAndSecuritySection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileSupportSection
import com.tyro.birthdayreminder.view_model.AuthViewModel
import com.tyro.birthdayreminder.view_model.SignupFormViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    navHostController: NavHostController,
    signupFormViewModel: SignupFormViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current

    val snackBarHostState = remember { SnackbarHostState() }

    val fullName by authViewModel.fullName.collectAsState()

    val formState by signupFormViewModel.formState.collectAsState()

    signupFormViewModel.onFullNameChange(fullName?: "")

    LaunchedEffect(Unit) {
        launch {
            signupFormViewModel.uiEvent.collect{event ->
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
                            Text("Edit Profile", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground,
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
                        Icon(imageVector = Icons.Default.Person,
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

                Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
                    Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Update Profile Details", color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(32.dp))

                            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                                    value = formState.fullName,
                                    onValueChange = {signupFormViewModel.onFullNameChange(it)},
                                    singleLine = true,
                                    placeholder = { Text("Enter new name")},
                                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = "") },
                                )

                                Button(onClick = {
                                    authViewModel.updateUserFullName(formState.fullName)
                                    focusManager.clearFocus()
                                }, modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16))
                                {
                                    Text("Update Data")
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                TextButton(onClick = {}, modifier = Modifier.fillMaxWidth()){
                                    Text("Not your account? Click to Logout", textAlign = TextAlign.Center)
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}