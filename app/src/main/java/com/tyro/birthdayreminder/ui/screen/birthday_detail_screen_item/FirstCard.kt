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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tyro.birthdayreminder.R

@Composable
fun FirstCard() {

    val zodiacSigns = mapOf(
        "Aries" to "♈",
        "Taurus" to "♉",
        "Gemini" to "♊",
        "Cancer" to "♋",
        "Leo" to "♌",
        "Virgo" to "♍",
        "Libra" to "♎",
        "Scorpio" to "♏",
        "Sagittarius" to "♐",
        "Capricorn" to "♑",
        "Aquarius" to "♒",
        "Pisces" to "♓"
    )

    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Card(modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(width = 1.dp,
                color = MaterialTheme.colorScheme.surface),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.White), shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),

            content = {

                Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(modifier = Modifier.fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                        .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                        Box(modifier = Modifier.background(shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface).size(60.dp, 60.dp), Alignment.Center){
                            Text("148", style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("days until next birthday", color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium)

                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp).padding(bottom = 24.dp).padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Row {
                            Column(
                                modifier = Modifier.background(color = Color(0xFFC408AB).copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(8.dp)
                                    ).border(width = 1.dp, color = Color(0xFFC408AB).copy(alpha = 0.4f), shape = RoundedCornerShape(8.dp))
                                    .weight(1f)
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(0.dp, alignment = Alignment.CenterVertically)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "date", tint = Color(0xFFC408AB))
                                    Spacer(Modifier.width(4.dp))
                                    Text("BIRTHDAY", color = Color(0xFFC408AB), style = MaterialTheme.typography.titleMedium)
                                }
                                Spacer(Modifier.height(8.dp))
                                Text("December 7", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                                Text("Sunday, December, 7", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                            }
                            Spacer(Modifier.width(24.dp))

                            Column(
                                modifier = Modifier.background(color = Color(0xFFC45908).copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(8.dp)
                                ).border(width = 1.dp, color = Color(0xFFC45908).copy(alpha = 0.4f), shape = RoundedCornerShape(8.dp))
                                    .weight(1f)
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(painter = painterResource(id = R.drawable.rounded_featured_seasonal_and_gifts_24), contentDescription = "date", tint = Color(0xFFC45908))
                                    Spacer(Modifier.width(4.dp))
                                    Text("TURNING", color = Color(0xFFC45908), style = MaterialTheme.typography.titleMedium)
                                }
                                Spacer(Modifier.height(8.dp))
                                Text("29 years old", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                                Text("Currently 28", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                        Column(
                            modifier = Modifier.background(color = Color(0xFF8030DA).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            ).border(width = 1.dp, color = Color(0xFF8030DA).copy(alpha = 0.4f), shape = RoundedCornerShape(8.dp))
                                .padding(16.dp).fillMaxWidth(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Column(Modifier.background(color = Color(0xFF8030DA).copy(alpha = 0.5f), shape = CircleShape).padding(4.dp)) {
                                    Text(zodiacSigns["Cancer"]?:"", style = MaterialTheme.typography.headlineMedium)
                                }
                                Column {
                                    Text("Sagittarius", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                                    Text("Adventurous • Optimistic", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                                }
                                Box(Modifier.border(width = 1.dp, shape = RoundedCornerShape(32.dp), color = Color(0xFF8030DA).copy(alpha = 0.2f)).padding(horizontal = 16.dp, vertical = 4.dp)){
                                    Text("Fire Sign", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }

                    }

                }
            }
        )
    }

}