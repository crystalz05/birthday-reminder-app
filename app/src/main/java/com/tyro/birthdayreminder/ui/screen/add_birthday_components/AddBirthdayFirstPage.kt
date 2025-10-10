package com.tyro.birthdayreminder.ui.screen.add_birthday_components

import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.custom_class.DatePickerModal
import com.tyro.birthdayreminder.entity.objects.ContactPhoto
import com.tyro.birthdayreminder.view_model.ContactFormViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBirthdayFirstPage(
    innerPadding: PaddingValues,
    contactFormViewModel: ContactFormViewModel
    ){

    val formState by contactFormViewModel.formState.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Family", "Parent", "Sibling", "Child", "Spouse", "Partner", "Friend", "Best Friend", "Close Friend", "Colleague", "Boss", "Neighbour", "Classmate")

    var photoOpened by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
            uri: Uri? ->
        photoOpened = false
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            contactFormViewModel.onImagePicked(bitmap)
        }
    }

    Column(modifier = Modifier
        .padding(innerPadding)
        .padding(16.dp)
        .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { millis ->
                    millis?.let {
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        contactFormViewModel.onBirthDateChange(formatter.format(Date(it)))
                    }
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }

        Card(modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.White), shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(1.dp),
            content = {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), contentAlignment = Alignment.Center
                ) {
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier
                            .size(110.dp, 110.dp)
                            .background(
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                                shape = CircleShape
                            ), Alignment.Center){
                            Box(Modifier
                                .size(100.dp, 100.dp)
                                .background(Color.White, shape = CircleShape), Alignment.Center, content = {})
                            if(formState.photo != null){
                                when (val photo = formState.photo) {
                                    is ContactPhoto.Local -> {
                                        Image(
                                            bitmap = photo.bitmap.asImageBitmap(),
                                            contentDescription = "Profile picture",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(100.dp)
                                                .align(Alignment.Center)
                                                .clip(CircleShape)
                                        )
                                    }
                                    else -> Unit
                                }
                            }
                            else {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(100.dp, 100.dp)
                                        .align(Alignment.Center)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = CircleShape
                                        )
                                        .padding(6.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            IconButton(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd),
                                onClick = {
                                    if(!photoOpened){
                                        photoOpened = true
                                        pickImageLauncher.launch("image/*")
                                    }
                                }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .background(color = MaterialTheme.colorScheme.primary.copy(0.5f), shape = CircleShape).padding(6.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("Tap to add a photo",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Normal)
                    }
                }
            }
        )


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
                        Icon(imageVector = Icons.Outlined.Person, contentDescription = "Personal Information", tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("Personal Information", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.labelLarge)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(modifier = Modifier
                        .fillMaxWidth(),
                        leadingIcon = { Icon(painter = painterResource(id = R.drawable.outline_person_heart_24), tint = MaterialTheme.colorScheme.primary, contentDescription = "Relationship") },
                        value = formState.fullName, onValueChange = {contactFormViewModel.fullNameChange(it)},
                        label = { Text("Full Name", color = MaterialTheme.colorScheme.onSurface.copy(0.2f)) },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
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

                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Box(modifier = Modifier.weight(1f)
                            .clickable { showDatePicker = true }){
                            TextField(modifier = Modifier.fillMaxWidth(),
                                leadingIcon = { Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "Birth Date",
                                    tint = MaterialTheme.colorScheme.primary) },
                                value = formState.birthday,
                                onValueChange = {},
                                placeholder = { Text("Birth Date", color = MaterialTheme.colorScheme.onSurface.copy(0.2f)) },
                                enabled = false,
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            GenderDropdownMenu(
                                selectedSex = formState.gender,
                                onSexSelected = {contactFormViewModel.onGenderChange(it)},
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {expanded = !expanded}) {

                        TextField(modifier = Modifier
                            .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                            .fillMaxWidth(),
                            leadingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            value = formState.relationship, onValueChange = {},
                            label = { Text("Relationship", color = MaterialTheme.colorScheme.onSurface.copy(0.2f)) },
                            readOnly = true,
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded, onDismissRequest = {expanded = false}
                        ) {
                            options.forEach{
                                    option -> DropdownMenuItem(text = { Text(option) },
                                onClick = {
                                    contactFormViewModel.onRelationshipChange(option)
                                    expanded = false

                                })
                            }
                        }
                    }
                }
            }
        )
        Card(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.White), shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(1.dp),

            content = {

                Column(modifier = Modifier
//                        .border(width = 1.dp, color = MaterialTheme.colorScheme.surfaceContainer)
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(16.dp)) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painter = painterResource(id = R.drawable.sharp_add_notes_24),
                            tint = MaterialTheme.colorScheme.primary, contentDescription = "Relationship")
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Personal Note",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                        value = formState.personalNote, onValueChange = {
                            contactFormViewModel.onPersonalNoteChange(it)},
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        placeholder = { Text("Add any special notes, preferences, or memories", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.2f)) },
                        maxLines = 10,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    HorizontalDivider(thickness = 1.dp)
                    Spacer(Modifier.height(8.dp))
                    Text("Optional: Add gift ideas, favorite things, or special memories", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdownMenu(
    selectedSex: String,
    onSexSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Male", "Female", "Other")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedSex,
            onValueChange = {},
            readOnly = true,
            label = { Text("Sex", color = MaterialTheme.colorScheme.onSurface.copy(0.2f)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.menuAnchor() // Required for dropdown positioning
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSexSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
