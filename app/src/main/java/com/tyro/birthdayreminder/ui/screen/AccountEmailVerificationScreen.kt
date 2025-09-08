package com.tyro.birthdayreminder.ui.screen

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tyro.birthdayreminder.auth.UiEvent
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.view_model.AuthViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountEmailVerificationScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        launch {
            authViewModel.refreshEmailVerification()
        }

        authViewModel.uiEvent.collect{ event ->
            when(event) {
                is UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                is UiEvent.Navigate -> navHostController.navigate(event.route){
                    popUpTo(Screen.EmailVerification.route) {inclusive = true}
                }
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
                            Text("Account Email Verification", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground,
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
                        Icon(imageVector = Icons.Default.Email,
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
                Text("Check your email for a verification link", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Spacer(Modifier.height(50.dp))
                Text("Click on the button below to resend the link", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Button(onClick = {authViewModel.resendEmailVerification()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Transparent),
                    shape = RoundedCornerShape(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                        Spacer(Modifier.width(8.dp))
                        Text("Resend Verification Email", style = MaterialTheme.typography.titleMedium)
                        Icon(imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight, contentDescription = null)
                    }

                }
            }
        }
    }
}