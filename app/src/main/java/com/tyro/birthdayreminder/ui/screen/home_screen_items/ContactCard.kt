package com.tyro.birthdayreminder.ui.screen.home_screen_items

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.custom_class.getYear
import com.tyro.birthdayreminder.custom_class.titleCase
import com.tyro.birthdayreminder.entity.Contact
import com.tyro.birthdayreminder.navigation.Screen
import kotlinx.datetime.Month
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContactCard(
    contact: Contact,
    navHostController: NavHostController
    ) {

    val currentYear = LocalDate.now().year
    val (months, days) = com.tyro.birthdayreminder.custom_class.getDaysLeft(contact.birthday)

    val daysLeftString = when {
        months == 0 && days == 0 -> "Today"
        months == 0 && days == 1 -> "Tomorrow"
        months == 0 -> "In $days day${if (days != 1) "s" else ""}"
        else -> "In $months month${if (months != 1) "s" else ""} $days day${if (days != 1) "s" else ""}"
    }

    val (month, day) = com.tyro.birthdayreminder.custom_class.getMonthAndDay(contact.birthday)
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = ripple(
    color = Color.Black.copy(alpha = 0.2f),
    bounded = true
    )
    ) { navHostController.navigate(Screen.BirthDayDetail.passContactId(contact.id)) }.padding(vertical = 24.dp)) {
        Box(modifier = Modifier.background(color = Color.Gray, shape = CircleShape).size(50.dp), Alignment.Center){

            AsyncImage(
                model = contact.photo,
                contentDescription = "Profile Photo",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.baseline_person_24),
                error = painterResource(id = R.drawable.baseline_person_24),
                modifier = Modifier.clip(CircleShape).size(100.dp)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(contact.fullName, color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
                Text("Turning ${currentYear- getYear(contact.birthday)}", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Light)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    titleCase("${Month(month)} $day"), modifier = Modifier
                        .border(border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)), shape = RoundedCornerShape(10.dp))
                        .background(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(horizontal = 12.dp, vertical = 2.dp), color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Light
                )
                Text(daysLeftString, modifier = Modifier.padding(end = 4.dp), color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Light)
            }
        }
    }

}