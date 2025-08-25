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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.ui.theme.BirthdayReminderTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBirthdaySecondPage(
    innerPadding: PaddingValues,
    phoneNumber: String,
    onPhoneNumberChange:(String)-> Unit,
    emailAddress: String,
    onEmailAddressChange:(String)-> Unit,
    instagram: String,
    onInstagramChange:(String)-> Unit,
    twitter: String,
    onTwitterChange:(String)-> Unit,
){
    Column(modifier = Modifier.padding(innerPadding)
        .padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        Card(modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.White), shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(1.dp),

            content = {

                Column(modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(16.dp)) {
                    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Outlined.Phone, contentDescription = "Contact Information", tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("Contact Information", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.labelLarge)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Add their contact details to stay connected", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)

                    Spacer(modifier = Modifier.height(8.dp))
                    TextFieldItem(fieldName = "Phone Number", fieldValue = phoneNumber, onChange = {onPhoneNumberChange(it)}, R.drawable.baseline_call_24)
                    Spacer(modifier = Modifier.height(16.dp))
                    TextFieldItem(fieldName = "Email Address", fieldValue = emailAddress, onChange = {onEmailAddressChange(it)}, R.drawable.baseline_email_24)
                    Spacer(modifier = Modifier.height(16.dp))
                    TextFieldItem(fieldName = "Instagram", fieldValue = instagram, onChange = {onInstagramChange(it)}, R.drawable.instagram_svgrepo_com)
                    Spacer(modifier = Modifier.height(16.dp))
                    TextFieldItem(fieldName = "Twitter", fieldValue = twitter, onChange = {onTwitterChange(it)}, R.drawable.twitter_svgrepo_com)
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(thickness = 1.dp)
                    Spacer(Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_lightbulb_24),
                            contentDescription = "",
                            tint = Color.Red,
                            modifier = Modifier.background(shape = CircleShape, color = Color.Yellow)
                                .padding(4.dp)

                            )
                        Column {
                            Text("Stay Connected", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                            Text("Adding contact information helps you reach out easily on their special day. All fields are optional.",
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

@Composable
fun TextFieldItem(fieldName: String, fieldValue: String, onChange: (String)-> Unit, icon: Int ){
    TextField(modifier = Modifier
        .fillMaxWidth(),
        leadingIcon = { Icon(painter = painterResource(id = icon), contentDescription = fieldName, tint = MaterialTheme.colorScheme.primary) },
        value = fieldValue, onValueChange = {onChange(it)},
        label = { Text(fieldName, color = MaterialTheme.colorScheme.onSurface.copy(0.2f)) },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(0.dp)
    )
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun AddBirthdaySecondPagePreview(){

    val phoneNumber = remember { mutableStateOf("") }
    val emailAddress = remember { mutableStateOf("") }
    val instagram = remember { mutableStateOf("") }
    val twitter = remember { mutableStateOf("") }

    BirthdayReminderTheme {
        AddBirthdaySecondPage(
            innerPadding = PaddingValues(4.dp),
            phoneNumber = phoneNumber.value,
            onPhoneNumberChange = {phoneNumber.value = it},
            emailAddress = phoneNumber.value,
            onEmailAddressChange = {emailAddress.value = it},
            instagram = phoneNumber.value,
            onInstagramChange = {instagram.value = it},
            twitter = phoneNumber.value,
            onTwitterChange = {twitter.value = it},
        )
    }
}
