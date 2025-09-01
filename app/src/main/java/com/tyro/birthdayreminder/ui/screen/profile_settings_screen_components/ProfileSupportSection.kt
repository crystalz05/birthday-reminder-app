package com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tyro.birthdayreminder.R

@Composable
fun ProfileSupportSection(){

    Spacer(modifier = Modifier.height(12.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(modifier = Modifier.background(color = MaterialTheme.colorScheme.primary,
            shape = CircleShape).padding(6.dp),
            painter = painterResource(id = R.drawable.outline_question_exchange_24), tint = MaterialTheme.colorScheme.onPrimary, contentDescription = null)
        Text("Support", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
    }
    Spacer(modifier = Modifier.height(12.dp))
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp),
        content = {
            Column(modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface),
            ) {
                SettingItemMisc(
                    settingName = "Help Center",
                    settingDescription = "Get help and support",
                    onClick = {}
                )
                HorizontalDivider(thickness = 1.dp)
                SettingItemMisc(
                    settingName = "Contact Us",
                    settingDescription = "Send feedback or report issues",
                    onClick = {}
                )
                HorizontalDivider(thickness = 1.dp)
                SettingItemMisc(
                    settingName = "Rate App",
                    settingDescription = "Rate us on Play Store",
                    onClick = {}
                )
            }
        }
    )
    Spacer(Modifier.height(8.dp))
}