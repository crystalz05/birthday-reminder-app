package com.tyro.birthdayreminder.ui.screen

import com.tyro.birthdayreminder.R
import android.content.res.Configuration
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.tyro.birthdayreminder.screen.add_birthday_components.AddBirthdaySecondPage
import com.tyro.birthdayreminder.screen.add_birthday_components.AddBirthdayThirdPage
import com.tyro.birthdayreminder.screen.add_birthday_components.EditBirthdayFirstPage
import com.tyro.birthdayreminder.screen.add_birthday_components.EditBirthdaySecondPage
import com.tyro.birthdayreminder.screen.add_birthday_components.EditBirthdayThirdPage
import com.tyro.birthdayreminder.ui.theme.BirthdayReminderTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBirthdayScreen() {

    var fullName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var instagram by remember { mutableStateOf("") }
    var twitter by remember { mutableStateOf("") }

    val canProceed by remember { derivedStateOf { fullName.isNotBlank() && selectedDate.isNotBlank()} }

    var currentTab by remember { mutableStateOf("Contact") }

    val tabs = listOf(
        "Personal" to Icons.Default.Person,
        "Contact" to Icons.Default.Call,
        "Reminder" to Icons.Default.Notifications
    )
    val selectedIndex = tabs.indexOfFirst { it.first == currentTab }

    Scaffold(
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
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(start = 10.dp, end = 20.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
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
                    Button(onClick = {},  shape = RoundedCornerShape(8.dp),
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
                            Column {
                                Text("Delete Birthday")
                                Text("Remove Sarah Johnson's birthday permanently",
                                    style = MaterialTheme.typography.labelSmall
                                    )
                            }
                            OutlinedButton(onClick = {}, shape = RoundedCornerShape(8.dp), border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.error),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor =  MaterialTheme.colorScheme.error)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                                    Text("Delete")
                                }

                            }
                        }
                    }
                )
            }
        }
    ) {innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {
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
                            Text("Currently editing - Sarah Johnson",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Medium)
                        }
                    }
                }
            )

            if(currentTab == "Personal"){
                EditBirthdayFirstPage(
                    selectedDate = selectedDate,
                    onSelectedDateChange = {
                        selectedDate = it
                    },
                    fullName = fullName,
                    onFullNameChange = {
                        fullName = it
                    }
                )
            }
            if(currentTab == "Contact"){
                EditBirthdaySecondPage(
                    phoneNumber = phoneNumber,
                    onPhoneNumberChange = {phoneNumber = it},
                    emailAddress = emailAddress,
                    onEmailAddressChange = {emailAddress = it},
                    instagram = instagram,
                    onInstagramChange = {instagram = it},
                    twitter = twitter,
                    onTwitterChange = {twitter = it}
                )
            }
            if(currentTab == "Reminder"){
                EditBirthdayThirdPage()
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun EditBirthdayScreenPreview(){
    BirthdayReminderTheme {
        EditBirthdayScreen()
    }
}