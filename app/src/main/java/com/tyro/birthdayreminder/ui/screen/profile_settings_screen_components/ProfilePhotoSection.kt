package com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components

import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.custom_class.ProfileActionMenu
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.view_model.AuthViewModel

@Composable
fun ProfilePhotoSection(
    navHostController: NavHostController,
    authViewModel: AuthViewModel
){

    val haptic = LocalHapticFeedback.current

    var showDialog by remember { mutableStateOf(false) }

    var photoOpened by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val imageUrl by authViewModel.imageUrl.collectAsState()
    val name by authViewModel.fullName.collectAsState()

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
            uri: Uri? ->
        photoOpened = false
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            authViewModel.updateProfilePhoto(bitmap)
        }
    }

    Card(modifier = Modifier
        .border(width = 2.dp, color = Color.Transparent, shape = RoundedCornerShape(6.dp)),
        content = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier
                        .size(110.dp, 110.dp)
                        .background(
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                            shape = CircleShape
                        ), Alignment.Center){
                        Box(Modifier
                            .size(100.dp, 100.dp)
                            .background(Color.White, shape = CircleShape), Alignment.Center, content = {})

                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Profile Photo",
                            placeholder = painterResource(id = R.drawable.baseline_person_24),
                            error = painterResource(id = R.drawable.baseline_person_24),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.Center)
                                .clip(CircleShape)
                        )

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
                                    .background(
                                        color = MaterialTheme.colorScheme.primary.copy(0.5f),
                                        shape = CircleShape
                                    )
                                    .padding(6.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(name ?: "unnamed",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Normal)
                    Text("mikebingp@gmail.com",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                        fontWeight = FontWeight.Normal)
                }
                Column(Modifier
                    .align(Alignment.TopEnd)
                ) {
                    ProfileActionMenu(
                        onRemovePhoto = {
                            authViewModel.deleteProfilePhoto()
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onEditAccount = {
                            navHostController.navigate(Screen.AccountVerification.route)
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                    )
                }
            }
        }
    )
    Spacer(Modifier.height(8.dp))
}