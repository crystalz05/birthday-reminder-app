package com.tyro.birthdayreminder.custom_class

import androidx.compose.foundation.background
import com.tyro.birthdayreminder.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ContactActionMenu(
    onEditBirthday: () -> Unit,
    onRemovePhoto: () -> Unit,
    onDeleteContact: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        // Button or Icon to open the dropdown
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.Transparent,
            shadowElevation = 0.dp,

        ) {
            // Edit Birthday
            DropdownMenuItem(
                text = {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.background(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer)
                                .padding(8.dp)
                        )
                    }
                },
                onClick = {
                    expanded = false
                    onEditBirthday()
                }
            )

            DropdownMenuItem(
                text = {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
//                        Text("Remove Photo")
                        Icon(painter = painterResource(id = R.drawable.outline_frame_person_off_24),
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = null,
                            modifier = Modifier.background(shape = CircleShape, color = MaterialTheme.colorScheme.errorContainer)
                                .padding(8.dp)
                        )
                    }
                },
                onClick = {
                    expanded = false
                    onRemovePhoto()
                }
            )

            DropdownMenuItem(
                text = {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
//                        Text("Remove Photo")
                        Icon(imageVector = Icons.Default.Delete,
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = null,
                            modifier = Modifier.background(shape = CircleShape, color = MaterialTheme.colorScheme.errorContainer)
                                .padding(8.dp)
                        )
                    }
                },
                onClick = {
                    expanded = false
                    onDeleteContact()
                }
            )
        }
    }
}


@Composable
fun ProfileActionMenu(
    onRemovePhoto: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.Transparent,
            shadowElevation = 0.dp,

            ) {
            DropdownMenuItem(
                text = {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                        Icon(painter = painterResource(id = R.drawable.outline_frame_person_off_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.background(shape = CircleShape, color = MaterialTheme.colorScheme.errorContainer)
                                .padding(8.dp)
                        )
                    }
                },
                onClick = {
                    expanded = false
                    onRemovePhoto()
                }
            )


        }
    }
}
