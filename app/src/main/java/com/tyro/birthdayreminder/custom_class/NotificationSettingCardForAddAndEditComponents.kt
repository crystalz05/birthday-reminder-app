package com.tyro.birthdayreminder.custom_class

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tyro.birthdayreminder.R

@Composable
fun NotificationSettingsCard(icon: Int, title: String, subTitle: String, active: Boolean, toggleActive: (Boolean)-> Unit){
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Icon(painter = painterResource(id = icon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .background(shape = CircleShape, color = MaterialTheme.colorScheme.surface)
                        .padding(8.dp)

                )
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            title, fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelLarge)
                        if(active){
                            Text("Active", style = MaterialTheme.typography.labelMedium,
                                color = colorResource(id = R.color.teal_200),
                                modifier = Modifier
                                    .border(
                                        shape = RoundedCornerShape(16.dp),
                                        width = 1.dp,
                                        color = colorResource(id = R.color.teal_200)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(subTitle, fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.labelSmall)
                }
            }
            CustomSwitch(
                checked = active,
                onCheckedChange = { toggleActive(it) }
            )

        }
    }
}