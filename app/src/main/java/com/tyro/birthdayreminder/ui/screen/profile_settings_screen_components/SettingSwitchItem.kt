package com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tyro.birthdayreminder.custom_class.CustomSwitch

@Composable
fun SettingSwitchItem(settingName: String, settingDescription: String, active: Boolean, onChange:(Boolean)-> Unit){

    Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                settingName,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = settingDescription, style = MaterialTheme.typography.labelMedium)
        }
        CustomSwitch(active, onCheckedChange = {onChange(it)})
    }
}