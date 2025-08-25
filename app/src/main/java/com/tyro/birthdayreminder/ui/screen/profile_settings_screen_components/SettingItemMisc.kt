package com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SettingItemMisc(settingName: String, settingDescription: String, onClick:()-> Unit){

    Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f)) {
            Text(modifier = Modifier.fillMaxWidth(),
                text = settingName,
                fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = settingDescription,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        IconButton(onClick = {onClick()}) {
            Icon(imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight, contentDescription = null)
        }

    }
}