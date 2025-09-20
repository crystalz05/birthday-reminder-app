package com.tyro.birthdayreminder.ui.screen

import com.tyro.birthdayreminder.R
import android.content.res.Configuration
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tyro.birthdayreminder.auth.ContactOperationState
import com.tyro.birthdayreminder.auth.UiEvent
import com.tyro.birthdayreminder.custom_class.Loading
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.screen.add_birthday_components.EditBirthdayFirstPage
import com.tyro.birthdayreminder.screen.add_birthday_components.EditBirthdaySecondPage
import com.tyro.birthdayreminder.screen.add_birthday_components.EditBirthdayThirdPage
import com.tyro.birthdayreminder.ui.screen.add_birthday_components.AddBirthdaySecondPage
import com.tyro.birthdayreminder.ui.screen.add_birthday_components.AddBirthdayThirdPage
import com.tyro.birthdayreminder.ui.theme.BirthdayReminderTheme
import com.tyro.birthdayreminder.view_model.BirthdayContactViewModel
import com.tyro.birthdayreminder.view_model.ContactFormViewModel
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBirthdayScreen(
    navHostController: NavHostController,
    contactId: String?,
    birthdayContactViewModel: BirthdayContactViewModel = hiltViewModel(),
    contactFormViewModel: ContactFormViewModel = hiltViewModel()
    ) {

    val snackBarHostState = remember { SnackbarHostState() }

    val formState by contactFormViewModel.formState.collectAsState()

    val canProceed by remember { derivedStateOf {
        formState.fullName.isNotBlank()
                && formState.birthday.isNotBlank()
                && formState.phoneNumber.isNotBlank()} }
    var currentTab by remember { mutableStateOf("Personal") }

    val tabs = listOf(
        "Personal" to Icons.Default.Person,
        "Contact" to Icons.Default.Call,
        "Reminder" to Icons.Default.Notifications
    )
    val selectedIndex = tabs.indexOfFirst { it.first == currentTab }

    LaunchedEffect(Unit) {
        if (contactId != null) {
            birthdayContactViewModel.loadSingleContact(contactId)
        }
    }

    val contactOperationState by birthdayContactViewModel.contactOperationState.collectAsState()
    val contact by birthdayContactViewModel.contactDetail.collectAsState()

    LaunchedEffect(contact) {
        contact?.let {
            contactFormViewModel.setFromContact(it)
        }
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
                        popUpTo(Screen.BirthDayDetail.passContactId(contactId)) { inclusive = true }
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
                    title = { Text("Edit Birthday", style = MaterialTheme.typography.titleLarge,
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
            Column(Modifier.navigationBarsPadding().padding(16.dp)
            ){
                Row(horizontalArrangement = Arrangement.spacedBy(18.dp), modifier = Modifier
                    ) {
                    OutlinedButton(onClick = {}, shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f)) {
                        Icon(imageVector = Icons.Filled.Clear,
                            contentDescription = "",
                            Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(16.dp))
                        Text("Cancel")
                    }
                    Button(onClick = {
                            if(contactFormViewModel.validateForm()){
                                if (contactId != null) {
                                    birthdayContactViewModel.updateContact(contactId, formState)
                                    Log.e("updateContact", "$contactId",)

                                }
                            }

                    },  shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        enabled = canProceed,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(painter = painterResource(id = R.drawable.sharp_save_as_24),
                            contentDescription = "Save Changes",
                            Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(16.dp))
                        Text("Save Changes")
                    }
                }
                Spacer(Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth().
                background(color = MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(8.dp)),
                    content = {
                        Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                                Text("Delete Birthday")
                                Text("Remove Sarah Johnson's birthday permanently",
                                    style = MaterialTheme.typography.labelSmall
                                    )
                            }
                            OutlinedButton(onClick = {}, shape = RoundedCornerShape(8.dp), border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.error),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor =  MaterialTheme.colorScheme.error)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                                    Text("Delete", maxLines = 1)
                                }

                            }
                        }
                    }
                )
            }
        }
    ) {innerPadding ->
        if (contact == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Loading()
            }
        }else{
            Column(modifier = Modifier.padding(innerPadding)) {
                Box(modifier = Modifier.fillMaxSize()){
                    Card(modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background,
                            contentColor = Color.White), shape = RoundedCornerShape(8.dp),
                        content = {
                            Box(modifier = Modifier.fillMaxWidth()
                                .padding(16.dp), contentAlignment = Alignment.CenterStart
                            ) {
                                TabRow(selectedTabIndex = selectedIndex) {
                                    tabs.forEachIndexed { index, (tabName, icon) ->
                                        Tab(
                                            selected = selectedIndex == index,
                                            onClick = { currentTab = tabName },
                                            text = { Text(tabName) },
                                            icon = { Icon(icon, contentDescription = null) } // make dynamic
                                        )
                                    }
                                }
                            }
                        }
                    )
                    Card(modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = Color.White), shape = RoundedCornerShape(0.dp),
                        content = {
                            Box(modifier = Modifier.fillMaxWidth()
                                .background(color = colorResource(id = R.color.orange))
                                .padding(16.dp), contentAlignment = Alignment.CenterStart
                            ) {
                                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
                                    Text("Currently editing - ${contact!!.fullName}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    )

                    if(currentTab == "Personal"){
                        EditBirthdayFirstPage()
                    }
                    if(currentTab == "Contact"){
                        EditBirthdaySecondPage()
                    }
                    if(currentTab == "Reminder"){
                        EditBirthdayThirdPage()
                    }

                    if (contactOperationState is ContactOperationState.Loading) {
                        Loading()
                    }
                }
            }
        }
    }
}

@Composable
fun TabItem(
    tabName: String,
    modifier: Modifier = Modifier,
    activeTab: Boolean,
    icon: ImageVector,
    switchTab: () -> Unit
) {
    Box(
        modifier
            .background(
                color = if (activeTab) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { switchTab() }
            .height(90.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = null)
            Text(tabName)
        }
    }
}

//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Composable
//fun EditBirthdayScreenPreview(){
//    BirthdayReminderTheme {
//        EditBirthdayScreen()
//    }
//}