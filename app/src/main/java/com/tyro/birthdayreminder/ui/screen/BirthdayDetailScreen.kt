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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayDetailScreen(
    navHostController: NavHostController,
    contactId: String?,
    birthdayContactViewModel: BirthdayContactViewModel = hiltViewModel(),
) {

    LaunchedEffect(Unit) {
        if (contactId != null) {
            birthdayContactViewModel.loadSingleContact(contactId)
        }
    }

    val contact by birthdayContactViewModel.contactDetail.collectAsState()
    val name = contact?.fullName
    val relationship = contact?.relationship
    val currentAge = contact?.birthday?.let { getAge(it) }
    val turningAge = contact?.birthday?.let { getAgeOnNextBirthday(it) }
    val (month, day) = contact?.birthday?.let { getMonthAndDay(it) } ?: (null to null)
    val dayOfWeek = contact?.birthday?.let { getDayOfWeek(it) }
    val (monthLeft, daysLeft) = contact?.birthday?.let { getDaysLeft(it) } ?: (null to null)
    val birthDayDateIntValue = contact?.birthday?.let { getDate(it) }

    val phoneNumber = contact?.phoneNumber
    val email = contact?.email
    val instagram = contact?.instagram

    Scaffold(
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
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
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
                    Box(){
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
                                    .size(100.dp, 100.dp)
                                    .background(Color.White, shape = CircleShape), Alignment.Center, content = {})
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp, 100.dp)
                                        .align(Alignment.Center) // places it at top-right corner of the Box
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = CircleShape
                                        )
                                        .padding(6.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
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
                            }
                            Spacer(Modifier.height(20.dp))
                        }
                        Column(Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 16.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                shape = CircleShape
                            )
                        ) {
                            IconButton(onClick = {navHostController.navigate(Screen.EditBirthDay.passContactId(contactId))}) {
                                Icon(
                                    imageVector = Icons.Default.Edit, contentDescription = null,
                                    tint = colorResource(id = R.color.orange)
                                )
                            }
                        }
                    }
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        FirstCard(monthLeft, daysLeft, month, day, dayOfWeek, currentAge, turningAge, birthDayDateIntValue)
                        SecondCard(phoneNumber, email, instagram)
                        ThirdCard()
                    }
                }
            }
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