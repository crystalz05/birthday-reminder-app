package com.tyro.birthdayreminder.ui.screen.home_screen_items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UpComingBirthdays() {

    val items = (1..8).toList()

    Card(modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background,
            contentColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ){
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.DateRange, contentDescription = "", tint = MaterialTheme.colorScheme.primary)
                Text("Upcoming Birthdays", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.height(24.dp))

            items.forEach { _ ->
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 24.dp)) {
                    Box(modifier = Modifier.background(color = Color.Gray, shape = CircleShape).size(50.dp), Alignment.Center){
                        Icon(Icons.Default.Person, contentDescription = "", Modifier.size(30.dp))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Emma Wilson", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Light, fontSize = 20.sp)
                            Text("Turning 24", color = MaterialTheme.colorScheme.onBackground, fontSize = 16.sp, fontWeight = FontWeight.Light)
                        }
                        Column {
                            Text("Dec 8", modifier = Modifier
                                .border(border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)), shape = RoundedCornerShape(10.dp))
                                .background(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                .padding(horizontal = 12.dp, vertical = 6.dp), color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Light
                            )
                            Text("3 days", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Light)
                        }
                    }
                }
                HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(start = 64.dp))
            }
        }
    }
}