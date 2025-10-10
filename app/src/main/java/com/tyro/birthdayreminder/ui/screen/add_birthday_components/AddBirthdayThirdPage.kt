package com.tyro.birthdayreminder.ui.screen.add_birthday_components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.custom_class.CustomSwitch
import com.tyro.birthdayreminder.custom_class.NotificationSettingsCard
import com.tyro.birthdayreminder.ui.theme.BirthdayReminderTheme
import com.tyro.birthdayreminder.view_model.ContactFormViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBirthdayThirdPage(
    paddingValues: PaddingValues,
    contactFormViewModel: ContactFormViewModel
){

    val formState by contactFormViewModel.formState.collectAsState()

    Column(modifier = Modifier
        .padding(paddingValues)
        .padding(16.dp)
        .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        var switch2Weeks = formState.reminders.contains("2w")
        var switch1Weeks = formState.reminders.contains("1w")
        var switch3Days = formState.reminders.contains("3d")
        var switchOnDay = formState.reminders.contains("0d")

        Card(modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.White), shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(1.dp),

            content = {

                Column(modifier = Modifier
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.surface)
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(16.dp)) {
                    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(id = R.drawable.baseline_notifications_active_24), contentDescription = "Contact Information", tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("Choose when you'd like to be reminded", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.labelLarge)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Add their contact details to stay connected", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)

                    Spacer(modifier = Modifier.height(16.dp))

                    NotificationSettingsCard(
                        R.drawable.baseline_calendar_month_24,
                        title = "2 Weeks Before",
                        subTitle = "Perfect time to start planning",
                        active = switch2Weeks,
                        toggleActive = { contactFormViewModel.on2WeeksToggled()  }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NotificationSettingsCard(
                        R.drawable.baseline_access_alarms_24,
                        title = "1 Week Before",
                        subTitle = "Get ready for the celebration",
                        active = switch1Weeks,
                        toggleActive = { contactFormViewModel.on1WeekToggled() }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NotificationSettingsCard(
                        R.drawable.baseline_notifications_active_24,
                        title = "3 Days Before",
                        subTitle = "Last chance to prepare",
                        active = switch3Days,
                        toggleActive = { contactFormViewModel.on3DaysToggled() }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NotificationSettingsCard(
                        R.drawable.baseline_phonelink_ring_24,
                        title = "On The Day",
                        subTitle = "Morning birthday reminder",
                        active = switchOnDay,
                        toggleActive = { contactFormViewModel.onDayToggled()}
                    )


                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_phonelink_ring_24),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(4.dp)
                            )
                        Column {
                            Text("Notification Settings", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                            Text("All reminders will be sent at 9:00 AM",
                                fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.labelMedium
                                )
                        }
                    }
                }
            }
        )
    }
}
//
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Composable
//fun AddBirthdayThirdPagePreview(){
//
//    BirthdayReminderTheme {
//        AddBirthdayThirdPage(PaddingValues(16.dp))
//    }
//}
