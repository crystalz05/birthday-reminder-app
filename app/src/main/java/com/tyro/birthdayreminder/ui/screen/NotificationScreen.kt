package com.tyro.birthdayreminder.ui.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import com.tyro.birthdayreminder.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.tyro.birthdayreminder.custom_class.Loading
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileAboutSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileAppearanceSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileNotificationSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfilePhotoSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfilePrivacyAndSecuritySection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileSupportSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.SettingSwitchItem
import com.tyro.birthdayreminder.view_model.AuthViewModel
import com.tyro.birthdayreminder.view_model.NotificationViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navHostController: NavHostController,
    notificationViewModel: NotificationViewModel
) {

    var activeTab by remember { mutableStateOf("All") }
    val tabs = listOf("All", "Unread")

//    val uiState by notificationViewModel.notificationUiState.collectAsState()

    val uiState by notificationViewModel.notificationUiState.collectAsStateWithLifecycle()

    val rawNotifications by notificationViewModel.notifications.collectAsState()

    val notifications = rawNotifications.sortedByDescending {  it.sent_at }

    LaunchedEffect(notifications) {
        notificationViewModel.getNotifications()
    }

    val haptic = LocalHapticFeedback.current
    var hasVibrated by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                TopAppBar(
                    title = {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("Notifications", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground,
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
                        Icon(imageVector = Icons.Default.Notifications,
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

        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
//            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//                tabs.forEach { tab ->
//                    NotificationTabItem(
//                        textContent = tab,
//                        active = activeTab == tab,
//                        onClicked = {activeTab = tab}
//                    )
//                }
//            }
            Spacer(Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.weight(1f)) {

                items(
                    items = notifications,
                    key = {it.id}
                ){ notification ->

                    val swipeToDismissBoxState  = rememberSwipeToDismissBoxState(
                        confirmValueChange = {value ->
                            when(value){
                                SwipeToDismissBoxValue.StartToEnd ->
                                    {notificationViewModel.deleteNotification(notification.id)
                                    true }
                                SwipeToDismissBoxValue.EndToStart ->  false
                                else -> false
                            }
                        },
                        positionalThreshold = { it * 0.8f }
                    )

                    LaunchedEffect(swipeToDismissBoxState.progress) {
                        val progress = swipeToDismissBoxState.progress

                        if (progress > 0.5f && !hasVibrated) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            hasVibrated = true
                        }

                        if (progress < 0.5f) {
                            hasVibrated = false // reset so it can trigger again on next swipe
                        }
                    }

                    SwipeToDismissBox(
                        state = swipeToDismissBoxState,
                        modifier = Modifier.fillMaxSize(),
                        backgroundContent = {
                            when(swipeToDismissBoxState.dismissDirection){
                                SwipeToDismissBoxValue.StartToEnd -> {
                                    Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Delete,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .wrapContentSize(Alignment.CenterStart)
                                                .padding(12.dp),
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                        Text("Deleting..", color = MaterialTheme.colorScheme.error)
                                    }
                                }
                                else -> {}
                            }
                        }
                    ) {
                        NotificationItem(
                            R.mipmap.ic_launcher_foreground,
                            notification.title,
                            notification.body,
                            notification.sent_at
                        )
                    }

//                    NotificationItem(
//                        R.mipmap.ic_launcher_foreground,
//                        notification.title,
//                        notification.body,
//                        notification.sent_at,
//                        deleteNotification = {
//                            notificationViewModel.deleteNotification(notification.id)
//                        }
//                    )
                    Spacer(Modifier.height(16.dp))

                }
            }
        }
    }
}

@Composable
fun NotificationItem(icon: Int, title: String, message: String, date: String){
    Card(modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(width = 0.3.dp, color = MaterialTheme.colorScheme.onSurface.copy(0.1f)),
        elevation = CardDefaults.cardElevation(1.dp),
        content = {
            Column(modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface),
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(id = icon),
                            tint = Color.Unspecified,
                            modifier = Modifier.size(48.dp), contentDescription = null)
                        Text(title, style = MaterialTheme.typography.titleMedium)
                    }
                    Text(message,
                        style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Normal)
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider(thickness = 0.4.dp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = date, overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Normal)
                }
            }
        }
    )
}

@Composable
fun NotificationTabItem(textContent: String, active: Boolean, onClicked:()->Unit){

    val bgColor = MaterialTheme.colorScheme.surfaceTint .copy(0.3f)
    val textColor = if(active){
        MaterialTheme.colorScheme.onSurface
    }else{
        MaterialTheme.colorScheme.onSurface
    }

    val border = if(active){
        BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceTint)
    }else null

    Text(textContent, modifier = Modifier
        .clickable { onClicked() }
        .background(color = bgColor, shape = RoundedCornerShape(100))
        .then(if(border != null) Modifier.border(shape = RoundedCornerShape(100), border = border) else Modifier)
        .padding(horizontal = 32.dp, vertical = 4.dp),
        color = textColor
    )
}