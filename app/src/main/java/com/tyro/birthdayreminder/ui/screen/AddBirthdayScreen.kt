package com.tyro.birthdayreminder.ui.screen

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tyro.birthdayreminder.auth.AuthState
import com.tyro.birthdayreminder.auth.ContactOperationState
import com.tyro.birthdayreminder.auth.UiEvent
import com.tyro.birthdayreminder.custom_class.Loading
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.ui.screen.add_birthday_components.AddBirthdayFirstPage
import com.tyro.birthdayreminder.ui.screen.add_birthday_components.AddBirthdaySecondPage
import com.tyro.birthdayreminder.ui.screen.add_birthday_components.AddBirthdayThirdPage
import com.tyro.birthdayreminder.ui.theme.BirthdayReminderTheme
import com.tyro.birthdayreminder.view_model.BirthdayContactViewModel
import com.tyro.birthdayreminder.view_model.ContactFormViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBirthdayScreen(
    navHostController: NavHostController,
    contactFormViewModel: ContactFormViewModel = hiltViewModel(),
    birthdayContactViewModel: BirthdayContactViewModel = hiltViewModel()
) {

    var canProceed by remember { mutableStateOf(false) }

    val snackBarHostState = remember { SnackbarHostState() }

    val formState by contactFormViewModel.formState.collectAsState()
    var currentPage by remember { mutableStateOf(FormPage.FIRST) }

    val contactOperationState by birthdayContactViewModel.contactOperationState.collectAsState()

    val nextLabel = when(currentPage){
        FormPage.FIRST, FormPage.SECOND -> "Next"
        FormPage.THIRD -> "Submit"
    }
    val prevEnabled = currentPage != FormPage.FIRST

    fun nextPage() {
        currentPage = when (currentPage) {
            FormPage.FIRST -> FormPage.SECOND
            FormPage.SECOND -> FormPage.THIRD
            FormPage.THIRD -> FormPage.THIRD
        }
    }

    fun prevPage() {
        currentPage = when (currentPage) {
            FormPage.FIRST -> FormPage.FIRST
            FormPage.SECOND -> FormPage.FIRST
            FormPage.THIRD -> FormPage.SECOND
        }
    }

    if (currentPage.equals(FormPage.FIRST) && formState.fullName.isNotBlank() && formState.birthday.isNotBlank()){
        canProceed = true
    }else if (currentPage.equals(FormPage.SECOND) && formState.phoneNumber.isNotBlank()){
        canProceed = true
    }else if( (currentPage.equals(FormPage.THIRD))){
        canProceed = true
    }else{
        canProceed = false
    }

    LaunchedEffect(Unit) {
        launch {
            contactFormViewModel.uiEvent.collect{event ->
                when(event){
                    is UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                    else -> Unit
                }
            }
        }

        launch {
            birthdayContactViewModel.uiEvent.collect{event ->
                when(event){
                    is UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                    is UiEvent.Navigate -> navHostController.navigate(event.route){
                        popUpTo(Screen.AddBirthDay.route) { inclusive = true }
                        launchSingleTop = true
                    }
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
                    title = { Text("Add Birthday", style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground) },
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        },
        bottomBar = {
            HorizontalDivider(thickness = 1.dp)
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp),
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(16.dp)) {
                OutlinedButton(onClick = { prevPage() },
                    enabled = prevEnabled,
                    shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f)) {
                    Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "",
                        Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text("Previous")
                }
                Button(onClick = {
                    when(currentPage){
                        FormPage.THIRD -> {
                            Log.d("viewmodel", "e.message.toString()")

                            if(contactFormViewModel.validateForm()){

                                birthdayContactViewModel.addContact(formState)

                            }
                        }
                        else -> nextPage()
                    }
                },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f),
                    enabled = canProceed,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(nextLabel)
                    Spacer(Modifier.width(16.dp))
                    Icon(imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = "",
                        Modifier.size(16.dp)
                    )
                }
            }
        }

    ) {innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize(), Alignment.Center){
            when(currentPage){
                FormPage.FIRST ->
                    AddBirthdayFirstPage(innerPadding)

                FormPage.SECOND ->
                    AddBirthdaySecondPage(innerPadding)

                FormPage.THIRD ->
                    AddBirthdayThirdPage(innerPadding)
            }
            if (contactOperationState is ContactOperationState.Loading) {
                Loading()
            }
        }

    }
}


enum class FormPage{
    FIRST, SECOND, THIRD
}
