package com.tyro.birthdayreminder.screen.add_birthday_components

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.ui.theme.BirthdayReminderTheme
import com.tyro.birthdayreminder.view_model.ContactFormViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBirthdaySecondPage(
    contactFormViewModel: ContactFormViewModel = hiltViewModel()
){

    val formState by contactFormViewModel.formState.collectAsState()

    Column(modifier = Modifier
        .padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        LazyColumn {
            item {
                Card(modifier = Modifier.shadow(elevation = 2.dp, shape = RoundedCornerShape(6.dp), clip = false)
                    .background(color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(6.dp))
                    .border(width = 2.dp, color = Color.Transparent, shape = RoundedCornerShape(6.dp)),
                    content = {

                        Column(modifier = Modifier
                            .border(width = 1.dp, color = MaterialTheme.colorScheme.surface)
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
                            TextFieldItemNumber(fieldName = "Phone Number", fieldValue = formState.phoneNumber, onChange = {contactFormViewModel.onPhoneNumberChange(it)}, R.drawable.baseline_call_24)
                            Spacer(modifier = Modifier.height(16.dp))
                            EditTextFieldItem(fieldName = "Email Address", fieldValue = formState.email, onChange = {contactFormViewModel.onEmailChange(it)}, R.drawable.baseline_email_24)
                            Spacer(modifier = Modifier.height(16.dp))
                            EditTextFieldItem(fieldName = "Instagram", fieldValue = formState.instagram, onChange = {contactFormViewModel.onInstagramChange(it)}, R.drawable.instagram_svgrepo_com)
                            Spacer(modifier = Modifier.height(16.dp))
                            EditTextFieldItem(fieldName = "Twitter", fieldValue = formState.twitter, onChange = {contactFormViewModel.onTwitterChange(it)}, R.drawable.twitter_svgrepo_com)
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
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun EditTextFieldItem(fieldName: String, fieldValue: String, onChange: (String)-> Unit, icon: Int ){
    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(modifier = Modifier.weight(1f)
            ,
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
        Icon(painter = painterResource(id = R.drawable.baseline_ios_share_24), contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun TextFieldItemNumber(fieldName: String, fieldValue: String, onChange: (String)-> Unit, icon: Int ){
    TextField(modifier = Modifier
        .fillMaxWidth(),
        leadingIcon = { Icon(painter = painterResource(id = icon), contentDescription = fieldName, tint = MaterialTheme.colorScheme.primary) },
        value = fieldValue, onValueChange = {onChange(it.filter { c -> c.isDigit() }) },
        label = { Text(fieldName, color = MaterialTheme.colorScheme.onSurface.copy(0.2f)) },
        singleLine = true,
        visualTransformation = phoneNumberVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

fun phoneNumberVisualTransformation(): VisualTransformation {
    return VisualTransformation { text ->
        val digits = text.text.filter { it.isDigit() }
        val formatted = buildString {
            for ((i, c) in digits.withIndex()) {
                append(c)
                if (i == 3 || i == 6) append(" ") // "080 123 4567" style
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Add a space after 3 and 6 digits
                return when {
                    offset <= 3 -> offset
                    offset <= 6 -> offset + 1
                    else -> offset + 2
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Reverse mapping
                return when {
                    offset <= 3 -> offset
                    offset <= 7 -> offset - 1
                    else -> offset - 2
                }
            }
        }

        TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}


//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Composable
//fun EditBirthdaySecondPagePreview(){
//
//    BirthdayReminderTheme {
//        EditBirthdaySecondPage()
//    }
//}
