package com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyro.birthdayreminder.view_model.AlarmViewModel
import com.tyro.birthdayreminder.view_model.BirthdayContactViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileNotificationSection(
    alarmViewModel: AlarmViewModel = hiltViewModel(),
    contactViewModel: BirthdayContactViewModel = hiltViewModel()
){

    var pushNotificationActive by remember { mutableStateOf(false) }
    var alarmActive by remember { mutableStateOf(false) }
    var emailNotificationActive by remember { mutableStateOf(false) }

    val alarmState by alarmViewModel.alarmState.collectAsState()
    val contacts by contactViewModel.contacts.collectAsState()


    Spacer(modifier = Modifier.height(12.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(modifier = Modifier.background(color = MaterialTheme.colorScheme.primary, shape = CircleShape).padding(6.dp),
            imageVector = Icons.Outlined.Notifications, tint = MaterialTheme.colorScheme.onPrimary, contentDescription = null)
        Text("Notifications", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
    }
    Spacer(modifier = Modifier.height(12.dp))
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp),
        content = {
            Column(modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface),
            ) {
                SettingSwitchItem(
                    settingName = "Push Notification",
                    settingDescription = "Receive birthday reminder",
                    active = pushNotificationActive,
                    onChange = {pushNotificationActive = it}
                )
                HorizontalDivider(thickness = 1.dp)
                SettingSwitchItem(
                    settingName = "Turn on alarm",
                    settingDescription = "Turn on alarm",
                    active = alarmState,
                    onChange = {alarmViewModel.toggleAlarmState(it, contacts)}
                )
                HorizontalDivider(thickness = 1.dp)
                SettingSwitchItem(
                    settingName = "Email Notification",
                    settingDescription = "Receive email reminder",
                    active = emailNotificationActive,
                    onChange = {emailNotificationActive = it}
                )
            }
        }
    )
    Spacer(Modifier.height(8.dp))
}