package com.tyro.birthdayreminder.ui.screen

import com.tyro.birthdayreminder.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileAboutSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileAppearanceSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileNotificationSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfilePhotoSection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfilePrivacyAndSecuritySection
import com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components.ProfileSupportSection


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navHostController: NavHostController) {


    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            ProfilePhotoSection(navHostController)
            Spacer(Modifier.height(16.dp))
            Spacer(Modifier.height(16.dp))
            Button(onClick = {},
                modifier = Modifier.border(width = 1.dp,
                    color = colorResource(id = R.color.red), shape = RoundedCornerShape(8.dp)
                )
                    .fillMaxWidth()
                    .background(color = Color.Transparent),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = null, tint = colorResource(id = R.color.red))
                    Spacer(Modifier.width(8.dp))
                    Text("Delete Account", style = MaterialTheme.typography.titleMedium, color = colorResource(id = R.color.red))
                }

            }
        }
    }

}