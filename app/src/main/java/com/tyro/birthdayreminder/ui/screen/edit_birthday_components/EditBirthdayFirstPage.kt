package com.tyro.birthdayreminder.screen.add_birthday_components

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.custom_class.DatePickerModal
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBirthdayFirstPage(
    selectedDate: String,
    onSelectedDateChange: (String) -> Unit,
    fullName: String,
    onFullNameChange: (String) -> Unit
    ){

    var personalNote by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Family", "Parent", "Sibling", "Child", "Spouse", "Partner", "Friend", "Best Friend", "Close Friend", "Colleague", "Boss", "Neighbour", "Classmate")
    var selectedOption by remember { mutableStateOf(options[0]) }

    Column(modifier = Modifier
        .padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { millis ->
                    millis?.let {
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        onSelectedDateChange(formatter.format(Date(it)))
                    }
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
        LazyColumn() {
            item() {
                Card(modifier = Modifier.shadow(elevation = 1.dp, shape = RoundedCornerShape(6.dp), clip = false)
                    .background(color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(6.dp))
                    .border(width = 2.dp, color = Color.Transparent, shape = RoundedCornerShape(6.dp))
                    .padding(16.dp),
                    content = {
                        Box(modifier = Modifier.fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.surface)
                            .padding(16.dp), contentAlignment = Alignment.Center
                        ) {
                            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(Modifier.size(110.dp, 110.dp).background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f), shape = CircleShape), Alignment.Center){
                                    Box(Modifier.size(100.dp, 100.dp).background(Color.White, shape = CircleShape), Alignment.Center, content = {})
                                    Icon(
                                        imageVector = Icons.Filled.Person,
                                        contentDescription = "",
                                        modifier = Modifier.size(100.dp, 100.dp)
                                            .align(Alignment.Center) // places it at top-right corner of the Box
                                            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape).padding(6.dp),
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_camera_24),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd) // places it at top-right corner of the Box
                                            .background(color = MaterialTheme.colorScheme.primary.copy(0.5f), shape = CircleShape).padding(6.dp),
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )

                                }
                                Spacer(Modifier.height(8.dp))
                                Text("Tap to change photo",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Normal)
                            }
                        }
                    }
                )
                Spacer(Modifier.height(16.dp))
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
                                Icon(imageVector = Icons.Outlined.Person, contentDescription = "Personal Information", tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(8.dp))
                                Text("Personal Information", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.labelLarge)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(modifier = Modifier
                                .fillMaxWidth(),
                                leadingIcon = { Icon(painter = painterResource(id = R.drawable.outline_person_heart_24), tint = MaterialTheme.colorScheme.primary, contentDescription = "Relationship") },
                                value = fullName, onValueChange = {onFullNameChange(it)},
                                label = { Text("Full Name", color = MaterialTheme.colorScheme.onSurface.copy(0.2f)) },
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
                            Box(modifier = Modifier
                                .clickable { showDatePicker = true }){
                                TextField(modifier = Modifier
                                    .fillMaxWidth(),
                                    leadingIcon = { Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "Birth Date",
                                        tint = MaterialTheme.colorScheme.primary) },
                                    value = selectedDate,
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
                            Spacer(modifier = Modifier.height(16.dp))

                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = {expanded = !expanded}) {

                                TextField(modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                                    .fillMaxWidth(),
                                    leadingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                    value = selectedOption, onValueChange = {},
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
                                            selectedOption = option
                                            expanded = false

                                        })
                                    }
                                }
                            }
                        }
                    }
                )
                Spacer(Modifier.height(16.dp))
                Card(modifier = Modifier.shadow(elevation = 2.dp, shape = RoundedCornerShape(6.dp), clip = false)
                    .background(color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(6.dp))
                    .border(width = 2.dp, color = Color.Transparent, shape = RoundedCornerShape(6.dp)),

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
                            TextField(modifier = Modifier.height(200.dp)
                                .fillMaxWidth(),
                                value = personalNote, onValueChange = {personalNote = it},
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
    }

}

