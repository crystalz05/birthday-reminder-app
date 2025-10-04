package com.tyro.birthdayreminder.ui.screen

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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.custom_class.getDayOfYear
import com.tyro.birthdayreminder.custom_class.getMonth
import com.tyro.birthdayreminder.custom_class.getMonthAndDay
import com.tyro.birthdayreminder.custom_class.getYear
import com.tyro.birthdayreminder.custom_class.titleCase
import com.tyro.birthdayreminder.entity.Contact
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.ui.screen.scaffold_contents.ScaffoldAction
import com.tyro.birthdayreminder.ui.screen.scaffold_contents.ScaffoldNavigation
import com.tyro.birthdayreminder.ui.screen.scaffold_contents.ScaffoldTitle
import com.tyro.birthdayreminder.view_model.BirthdayContactViewModel
import kotlinx.datetime.Month
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContactListScreen(
    listType: String?,
    navHostController: NavHostController,
    birthdayContactViewModel: BirthdayContactViewModel
) {

    val title: String = when(listType){
        "all_contacts" -> "All Contacts"
        "this_month" -> "This Month"
        else -> ""
    }

    val contacts by birthdayContactViewModel.contacts.collectAsState()

    val currentList: List<Contact> =
        if (listType == "this_month") {
            contacts.filter { contact ->
                getMonth(contact.birthday) == LocalDate.now().monthValue
            }
        } else {
            contacts
        }


    fun daysLeft(yearDay: String): Int{
        return getDayOfYear(yearDay) - LocalDate.now().dayOfYear
    }
    val currentYear = LocalDate.now().year


    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                TopAppBar(
                    title = { ScaffoldTitle("Contact List") },
                    navigationIcon = { ScaffoldNavigation(navHostController) },
                    actions = {ScaffoldAction(Icons.Default.Person) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        }

    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Card(modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background,
                    contentColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ){
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.DateRange, contentDescription = "", tint = MaterialTheme.colorScheme.primary)
                        Text(title, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleLarge)
                    }
                    Spacer(Modifier.height(24.dp))

                    currentList.forEach { contact ->
                        val (month, day) = getMonthAndDay(contact.birthday)
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(
                                        color = Color.Black.copy(alpha = 0.2f),
                                        bounded = true
                                    )
                                ) {
                                    navHostController.navigate(
                                        Screen.BirthDayDetail.passContactId(
                                            contact.id
                                        )
                                    )
                                }
                                .padding(vertical = 24.dp)) {
                            Box(modifier = Modifier
                                .background(color = Color.Gray, shape = CircleShape)
                                .size(50.dp), Alignment.Center){

                                AsyncImage(
                                    model = contact.photo,
                                    contentDescription = "Profile Photo",
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(id = R.drawable.baseline_person_24),
                                    error = painterResource(id = R.drawable.baseline_person_24),
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(100.dp)
                                )
                            }
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text(contact.fullName, color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
                                    Text("Turning ${currentYear-getYear(contact.birthday)}", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Light)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        titleCase("${Month(month)} $day"), modifier = Modifier
                                            .border(
                                                border = BorderStroke(
                                                    1.dp,
                                                    color = MaterialTheme.colorScheme.primary.copy(
                                                        alpha = 0.6f
                                                    )
                                                ), shape = RoundedCornerShape(10.dp)
                                            )
                                            .background(
                                                shape = RoundedCornerShape(10.dp),
                                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                            )
                                            .padding(horizontal = 12.dp, vertical = 2.dp), color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Light
                                    )
                                    Text("In ${daysLeft(contact.birthday)} days", modifier = Modifier.padding(end = 4.dp), color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Light)
                                }
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(start = 64.dp))
                    }
                }
            }
        }

    }


}