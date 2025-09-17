package com.tyro.birthdayreminder.ui.screen.birthday_detail_screen_item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tyro.birthdayreminder.R

@Composable
fun SecondCard(phoneNumber: String?, email: String?, instagram: String?){

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Card(modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(width = 1.dp,
                color = MaterialTheme.colorScheme.surface),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.White), shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),

            content = {
                Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Stay Connected", style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        Row {
                            MiniCardItem(Modifier.weight(1f), R.drawable.baseline_call_24,
                                "Call", phoneNumber ?:"No value", Color(0xFF2B7C0E)
                            )
                            Spacer(Modifier.width(24.dp))
                            MiniCardItem(Modifier.weight(1f), R.drawable.round_chat_24,
                                "Message", phoneNumber ?:"No value", Color(0xFF125296)
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        Row {
                            MiniCardItem(Modifier.weight(1f), R.drawable.baseline_email_24,
                                "Email", email ?:"No value", Color(0xFF683EA9)
                            )
                            Spacer(Modifier.width(24.dp))
                            MiniCardItem(Modifier.weight(1f), R.drawable.instagram_svgrepo_com,
                                "Instagram", "No link", Color(0xFFC408AB))
                        }

                    }

                }
            }
        )
    }

}

@Composable
fun MiniCardItem(modifier: Modifier, icon: Int, title: String, description: String, color: Color){
    Column(
        modifier.background(color = color.copy(alpha = 0.1f),
            shape = RoundedCornerShape(8.dp)
        ).border(width = 1.dp, color = color.copy(alpha = 0.4f), shape = RoundedCornerShape(8.dp))
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(painterResource(id = icon), contentDescription = "date", tint = color)
        Spacer(Modifier.height(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, color = color)
        Text(description, modifier = Modifier.padding(horizontal = 24.dp), style = MaterialTheme.typography.labelMedium,
            color = color, overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}