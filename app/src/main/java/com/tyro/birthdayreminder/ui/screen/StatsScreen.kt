package com.tyro.birthdayreminder.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.custom_class.getAge
import com.tyro.birthdayreminder.custom_class.getDaysLeft
import com.tyro.birthdayreminder.custom_class.getDaysLeftActualNumbers
import com.tyro.birthdayreminder.custom_class.getMonth
import com.tyro.birthdayreminder.custom_class.getWeekOfMonth
import com.tyro.birthdayreminder.ui.screen.stat_screen_items.PercentageBar
import com.tyro.birthdayreminder.ui.screen.stat_screen_items.PercentageBarWithText
import com.tyro.birthdayreminder.ui.screen.stat_screen_items.PercentageBarWithTextForAgeDistribution
import com.tyro.birthdayreminder.ui.screen.stat_screen_items.UpcomingBirthdayItem
import com.tyro.birthdayreminder.view_model.BirthdayContactViewModel
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    navHostController: NavHostController,
    birthdayContactViewModel: BirthdayContactViewModel = hiltViewModel()
) {

    var birthdayDistributionexpanded by remember { mutableStateOf(false) }
    var relationshipTypeExpanded by remember { mutableStateOf(false) }
    var upcomingBirthdaysExpanded by remember { mutableStateOf(false) }
    var ageDistributionExpanded by remember { mutableStateOf(false) }

    val dummyMonths = (1..12).toList()
    val dummyUpcomingList = (1..5).toList()

    val totalContacts by birthdayContactViewModel.contacts.collectAsState()
    val thisMonthBirthdays = totalContacts.filter { contact -> getMonth(contact.birthday) == LocalDate.now().monthValue }

    val currentYear = LocalDate.now().year
    val currentMonth = LocalDate.now().monthValue
    val currentWeekOfMonth = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfMonth())
    val thisMonthWeekBirthdays = totalContacts.filter { contact ->
        getMonth(contact.birthday) == currentMonth &&
                getWeekOfMonth(contact.birthday) == currentWeekOfMonth
    }

    val groupedContacts = totalContacts.groupBy { contact ->
        val age = getAge(contact.birthday)
        when (age) {
            in 0..15 -> "0 - 15"
            in 16..25 -> "16 - 25"
            in 26..40 -> "26 - 40"
            in 41..55 -> "41 - 55"
            in 56..70 -> "56 - 70"
            in 71..85 -> "71 - 85"
            else -> "85+"
        }
    }

    val monthDistributionGroups = totalContacts.groupBy { contact ->
        getMonth(contact.birthday)
    }

    fun birthdaysCount(month: Int): Float{
        val count = totalContacts.filter { contact -> getMonth(contact.birthday) == Month.of(month).value }
        return count.size/totalContacts.size.toFloat()
    }

    val relationshipGroups = totalContacts.groupingBy { it.relationship }.eachCount()
    val contactSorted = totalContacts
        .sortedBy { getDaysLeftActualNumbers(it.birthday) }
        .filter { getDaysLeftActualNumbers(it.birthday) <= 30 }

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
                            Text("Analytics - 2025", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground,
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
                        Icon(painter = painterResource(id = R.drawable.outline_analytics_24),
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

    ) {innerPadding ->
        LazyColumn(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)) {
            item {
                Column {
                    Row {
                        MiniStatCardItem(
                            Modifier.weight(1f), R.drawable.outline_contacts_24,
                            "${totalContacts.size}", "Total Contacts",
                        )
                        Spacer(Modifier.width(12.dp))
                        MiniStatCardItem(
                            Modifier.weight(1f), R.drawable.baseline_calendar_month_24,
                            "${thisMonthBirthdays.size}", "This Month"
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Row {
                        MiniStatCardItem(
                            Modifier.weight(1f), R.drawable.baseline_access_alarms_24,
                            "${thisMonthWeekBirthdays.size}", "This Week",
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Column(modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { birthdayDistributionexpanded = !birthdayDistributionexpanded}
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(6.dp), clip = false)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp, alignment = Alignment.CenterVertically),
                ) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(painter = painterResource(id = R.drawable.outline_analytics_24),
                                contentDescription = null,
                                tint = colorResource(id = R.color.blue)
                            )
                            Text("Birth Distribution", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                        }
                        IconButton(onClick = {birthdayDistributionexpanded = !birthdayDistributionexpanded}){
                            Icon(imageVector =
                                if(birthdayDistributionexpanded){
                                    Icons.Default.KeyboardArrowDown
                                }else{
                                    Icons.Default.KeyboardArrowUp
                                }, contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Text("Birthdays by month $currentYear")

                    AnimatedVisibility(visible = birthdayDistributionexpanded) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Spacer(Modifier.height(12.dp))
                            monthDistributionGroups.forEach { (month, contactsInMonth) ->
                                val percentage = contactsInMonth.size / totalContacts.size.toFloat()
                                if (percentage > 0f) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            Month.of(month).name.take(3),
                                            modifier = Modifier.width(50.dp),
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                        PercentageBarWithText(percentage)
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Column(modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { relationshipTypeExpanded = !relationshipTypeExpanded }
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(6.dp), clip = false)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp, alignment = Alignment.CenterVertically),
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(painter = painterResource(id = R.drawable.outline_person_heart_24),
                                contentDescription = null,
                                tint = colorResource(id = R.color.orange)
                            )
                            Text("Relationship Types", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                        }
                        IconButton(onClick = {relationshipTypeExpanded = !relationshipTypeExpanded}){
                            Icon(imageVector =
                                if(relationshipTypeExpanded){
                                    Icons.Default.KeyboardArrowDown
                                }else{
                                    Icons.Default.KeyboardArrowUp
                                }, contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Text("Distribution by relationship")

                    AnimatedVisibility(visible = relationshipTypeExpanded) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Spacer(Modifier.height(12.dp))
                            relationshipGroups.forEach{(relation, count) ->
                                Row {
                                    if (relation != null) {
                                        PercentageBar(count/totalContacts.size.toFloat(), relation, count)
                                    }
                                }
                            }

                            HorizontalDivider(thickness = 1.dp)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Icon(painter = painterResource(id = R.drawable.outline_contacts_24), contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Total: ${totalContacts.size} contacts", style = MaterialTheme.typography.labelLarge)
                            }
                        }

                    }
                }
                Spacer(Modifier.height(12.dp))
                Column(modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { upcomingBirthdaysExpanded = !upcomingBirthdaysExpanded }
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(6.dp), clip = false)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp, alignment = Alignment.CenterVertically),
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                                contentDescription = null,
                                tint = colorResource(id = R.color.green)
                            )
                            Text("Upcoming Birthdays", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                        }
                        IconButton(onClick = {upcomingBirthdaysExpanded = !upcomingBirthdaysExpanded}){
                            Icon(imageVector =
                                if(upcomingBirthdaysExpanded){
                                    Icons.Default.KeyboardArrowDown
                                }else{
                                    Icons.Default.KeyboardArrowUp
                                }, contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Text("Next celebrations")

                    AnimatedVisibility(visible = upcomingBirthdaysExpanded) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Spacer(Modifier.height(12.dp))
                            contactSorted.forEach { contact ->
                                Row {
                                    UpcomingBirthdayItem(contact)
                                }
                            }
                            HorizontalDivider(thickness = 1.dp)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Icon(painter = painterResource(id = R.drawable.baseline_access_alarms_24), contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Next 30 days: ${contactSorted.size} birthdays", style = MaterialTheme.typography.labelLarge)
                            }

                        }

                    }
                }
                Spacer(Modifier.height(12.dp))
                Column(modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { ageDistributionExpanded = !ageDistributionExpanded }
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(6.dp), clip = false)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp, alignment = Alignment.CenterVertically),
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(painter = painterResource(id = R.drawable.baseline_trending_up_24),
                                contentDescription = null,
                                tint = colorResource(id = R.color.purple)
                            )
                            Text("Age Distribution", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                        }
                        IconButton(onClick = {ageDistributionExpanded = !ageDistributionExpanded}){
                            Icon(imageVector =
                                if(ageDistributionExpanded){
                                    Icons.Default.KeyboardArrowDown
                                }else{
                                    Icons.Default.KeyboardArrowUp
                                }, contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Text("Contacts by age group")

                    AnimatedVisibility(visible = ageDistributionExpanded) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Spacer(Modifier.height(12.dp))
                            groupedContacts.forEach { (group, contactItem) ->
                                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                    PercentageBarWithTextForAgeDistribution(contactItem.size/totalContacts.size.toFloat(), group, contactItem.size)
                                }
                            }
                            HorizontalDivider(thickness = 1.dp)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                Icon(painter = painterResource(id = R.drawable.baseline_access_alarms_24), contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("A total ${totalContacts.size} birthdays", style = MaterialTheme.typography.labelLarge)
                            }

                        }

                    }
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun MiniStatCardItem(modifier: Modifier, icon: Int, title: String, description: String){
    Column(
        modifier
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(6.dp), clip = false)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(6.dp)
            )
            .border(width = 2.dp, color = Color.Transparent, shape = RoundedCornerShape(6.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Column(
            modifier = Modifier
                .background(
                    shape = CircleShape, color = colorResource(id = R.color.orange)
                )
                .padding(8.dp),
        ) {
            Icon(painterResource(id = icon), contentDescription = "date", tint = MaterialTheme.colorScheme.onPrimary)
        }
        Spacer(Modifier.height(8.dp))
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        Text(description, modifier = Modifier.padding(horizontal = 24.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer, overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}
