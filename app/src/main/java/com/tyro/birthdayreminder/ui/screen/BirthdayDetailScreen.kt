package com.tyro.birthdayreminder.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import com.tyro.birthdayreminder.R
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.tyro.birthdayreminder.auth.ContactOperationState
import com.tyro.birthdayreminder.auth.UiEvent
import com.tyro.birthdayreminder.custom_class.ContactActionMenu
import com.tyro.birthdayreminder.custom_class.Loading
import com.tyro.birthdayreminder.custom_class.getAge
import com.tyro.birthdayreminder.custom_class.getAgeOnNextBirthday
import com.tyro.birthdayreminder.custom_class.getDate
import com.tyro.birthdayreminder.custom_class.getDayOfWeek
import com.tyro.birthdayreminder.custom_class.getDaysLeft
import com.tyro.birthdayreminder.custom_class.getMonthAndDay
import com.tyro.birthdayreminder.entity.Contact
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.repository.BirthdayContactRepository
import com.tyro.birthdayreminder.ui.screen.birthday_detail_screen_item.FirstCard
import com.tyro.birthdayreminder.ui.screen.birthday_detail_screen_item.SecondCard
import com.tyro.birthdayreminder.ui.screen.birthday_detail_screen_item.ThirdCard
import com.tyro.birthdayreminder.ui.theme.BirthdayReminderTheme
import com.tyro.birthdayreminder.view_model.AuthViewModel
import com.tyro.birthdayreminder.view_model.BirthdayContactViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayDetailScreen(
    navHostController: NavHostController,
    contactId: String?,
    birthdayContactViewModel: BirthdayContactViewModel = hiltViewModel(),
) {

    LaunchedEffect(contactId) {
        if (contactId != null) {
            birthdayContactViewModel.loadSingleContact(contactId)
        }
    }

    val contact by birthdayContactViewModel.contactDetail.collectAsState()
    val name = contact?.fullName
    val relationship = contact?.relationship
    val gender = contact?.gender
    val turningAge = contact?.birthday?.let { getAgeOnNextBirthday(it) }
    val photoUrl = contact?.photo

    val phoneNumber = contact?.phoneNumber
    val email = contact?.email
    val instagram = contact?.instagram

    val userId = contact?.userId

    var showDialog by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val snackBarHostState = remember { SnackbarHostState() }

    val contactState by birthdayContactViewModel.contactOperationState.collectAsState()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
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



    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                TopAppBar(
                    title = { Text("Birthday Details", style = MaterialTheme.typography.titleLarge,
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
        }

    ) {innerPadding ->
        when(contactState){
            is ContactOperationState.Loading -> {
                Loading()
            }
            is ContactOperationState.Error ->{
                val errorMessage = (contactState as ContactOperationState.Error).message
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(errorMessage, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = {
                            if (contactId != null) {
                                birthdayContactViewModel.loadSingleContact(contactId)
                            }
                        }) {
                            Text("Retry")
                        }
                    }
                }
            }
            is ContactOperationState.Success <*> ->{
                if(contact != null){
                    LazyColumn( state = listState, modifier = Modifier.padding(innerPadding)) {

                        item {
                            Box(modifier = Modifier.fillMaxSize()){
                                if (showDialog) {
                                    AlertDialog(
                                        onDismissRequest = { showDialog = false },
                                        confirmButton = {
                                            TextButton(onClick = {
                                                if (contactId != null) {
                                                    birthdayContactViewModel.deleteContact(contactId)
                                                }
                                                showDialog = false
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
                                        title = { Text("Confirm Delete") },
                                        text = { Text("Are you sure you want to delete this contact?") }
                                    )
                                }
                                Column(modifier = Modifier
                                    .padding(bottom = 20.dp)
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(Modifier
                                        .size(110.dp, 110.dp)
                                        .background(
                                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                                            shape = CircleShape
                                        ), Alignment.Center){
                                        Box(Modifier
                                            .size(110.dp, 110.dp)
                                            .background(
                                                MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                    alpha = 0.2f
                                                ), shape = CircleShape
                                            ), Alignment.Center){
                                            Box(Modifier
                                                .size(100.dp, 100.dp)
                                                .background(Color.White, shape = CircleShape), Alignment.Center, content = {})
                                            AsyncImage(
                                                modifier = Modifier
                                                    .size(100.dp)
                                                    .alpha(1f)
                                                    .align(Alignment.Center)
                                                    .clip(CircleShape),
                                                model = photoUrl,
                                                contentDescription = "Profile Photo",
                                                placeholder = painterResource(id = R.drawable.baseline_person_24),
                                                error = painterResource(id = R.drawable.baseline_person_24),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Text(name ?: "",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.height(8.dp))
                                    Row {
                                        Text(relationship ?: "Unknown", style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.SemiBold, color= MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier
                                                .background(
                                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                                                    shape = RoundedCornerShape(16.dp)
                                                )
                                                .padding(horizontal = 12.dp, vertical = 4.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("Turning $turningAge", style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.SemiBold, color= MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier
                                                .background(
                                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                                                    shape = RoundedCornerShape(16.dp)
                                                )
                                                .padding(horizontal = 12.dp, vertical = 4.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text("$gender", style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.SemiBold, color= MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier
                                                .background(
                                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                                                    shape = RoundedCornerShape(16.dp)
                                                )
                                                .padding(horizontal = 12.dp, vertical = 4.dp))
                                    }
                                    Spacer(Modifier.height(20.dp))
                                }
                                Column(Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(end = 16.dp)
                                ) {
                                    ContactActionMenu(
                                        onEditBirthday = {
                                            navHostController.navigate(Screen.EditBirthDay.passContactId(contactId))
                                        },
                                        onRemovePhoto = {
                                            if (contactId != null) {
                                                if (userId != null) {
                                                    birthdayContactViewModel.deleteContactPhoto(userId, contactId){
                                                        birthdayContactViewModel.loadSingleContact(contactId)
                                                    }
                                                }
                                            }
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        },
                                        onDeleteContact = {
                                            showDialog = true
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        },
                                    )
                                }
                            }
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                FirstCard(birthdayContactViewModel)
                                SecondCard(phoneNumber, email, instagram)
                                if (contactId != null) {
                                    ThirdCard(
                                        contactId = contactId,
                                        birthdayContactViewModel = birthdayContactViewModel,
                                        onSectionSelected = { section ->
                                            coroutineScope.launch {
                                                listState.animateScrollToItem(listState.layoutInfo.totalItemsCount - 0)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            else -> Unit
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun BirthdayDetailScreenPreview(){
//    BirthdayReminderTheme{
//        BirthdayDetailScreen()
//    }
//}