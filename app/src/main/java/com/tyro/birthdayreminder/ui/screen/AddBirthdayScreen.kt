package com.tyro.birthdayreminder.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tyro.birthdayreminder.ui.screen.add_birthday_components.AddBirthdayFirstPage
import com.tyro.birthdayreminder.ui.screen.add_birthday_components.AddBirthdaySecondPage
import com.tyro.birthdayreminder.ui.screen.add_birthday_components.AddBirthdayThirdPage
import com.tyro.birthdayreminder.ui.theme.BirthdayReminderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBirthdayScreen(navHostController: NavHostController) {

    var fullName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var instagram by remember { mutableStateOf("") }
    var twitter by remember { mutableStateOf("") }

    val canProceed by remember { derivedStateOf { fullName.isNotBlank() && selectedDate.isNotBlank()} }

    Scaffold(
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
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp), modifier = Modifier
                .navigationBarsPadding()
                .padding(16.dp)) {
                OutlinedButton(onClick = {}, shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f)) {
                    Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "",
                        Modifier.size(16.dp)
                        )
                    Spacer(Modifier.width(16.dp))
                    Text("Previous")
                }
                Button(onClick = {},  shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f),
                    enabled = canProceed,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Next")
                    Spacer(Modifier.width(16.dp))
                    Icon(imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = "",
                        Modifier.size(16.dp)
                        )
                }
            }
        }


    ) {innerPadding ->
            AddBirthdayFirstPage(
                innerPadding,
                selectedDate = selectedDate,
                onSelectedDateChange = {
                    selectedDate = it
                },
                fullName = fullName,
                onFullNameChange = {
                    fullName = it
                }
            )

//        AddBirthdaySecondPage(
//            innerPadding,
//            phoneNumber = phoneNumber,
//            onPhoneNumberChange = {phoneNumber = it},
//            emailAddress = emailAddress,
//            onEmailAddressChange = {emailAddress = it},
//            instagram = instagram,
//            onInstagramChange = {instagram = it},
//            twitter = twitter,
//            onTwitterChange = {twitter = it}
//        )
//
//        AddBirthdayThirdPage(
//            innerPadding
//        )
    }
}