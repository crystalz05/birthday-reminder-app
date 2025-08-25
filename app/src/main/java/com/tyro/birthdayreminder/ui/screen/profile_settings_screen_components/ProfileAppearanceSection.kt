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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun ProfileAppearanceSection(){

    var darkModeActive by remember { mutableStateOf(false) }
    var autoThemeActive by remember { mutableStateOf(false) }


    Spacer(modifier = Modifier.height(12.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(modifier = Modifier.background(color = MaterialTheme.colorScheme.primary,
            shape = CircleShape).padding(6.dp),
            painter = painterResource(id = R.drawable.baseline_format_paint_24), tint = MaterialTheme.colorScheme.onPrimary, contentDescription = null)
        Text("Appearance", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
    }
    Spacer(modifier = Modifier.height(12.dp))
    Card(modifier = Modifier.fillMaxWidth().shadow(elevation = 2.dp, shape = RoundedCornerShape(6.dp), clip = false)
        .background(color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(18.dp)
        )
        .border(width = 2.dp, color = Color.Transparent, shape = RoundedCornerShape(6.dp)),
        shape = RectangleShape,
        content = {
            Column(modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface),
            ) {
                SettingSwitchItem(
                    settingName = "Dark Mode",
                    settingDescription = "Use dark theme",
                    active = darkModeActive,
                    onChange = {darkModeActive = it}
                )
                HorizontalDivider(thickness = 1.dp)
                SettingSwitchItem(
                    settingName = "Auto Theme",
                    settingDescription = "Follow system theme",
                    active = autoThemeActive,
                    onChange = {autoThemeActive = it}
                )
            }
        }
    )
    Spacer(Modifier.height(8.dp))
}