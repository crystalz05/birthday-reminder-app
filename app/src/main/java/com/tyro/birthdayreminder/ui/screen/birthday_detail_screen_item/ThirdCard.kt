package com.tyro.birthdayreminder.ui.screen.birthday_detail_screen_item

import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tyro.birthdayreminder.R

@Composable
fun ThirdCard(){

    val wishlist = mutableListOf("Japanese cooking class", "Chocolate making kit", "Travel journal")

    var active by remember { mutableStateOf("Journal") }

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Card(modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(width = 1.dp,
                color = MaterialTheme.colorScheme.surface),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.White), shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)){


            Row(Modifier.fillMaxWidth().padding(16.dp)) {
                ItemsCard(Modifier.weight(1f).clickable { active="Wish List" }, R.drawable.gift_box_svgrepo_com,
                    "Wish List", active == "Wish List")
                Spacer(Modifier.width(16.dp))
                ItemsCard(Modifier.weight(1f).clickable { active="Journal" }, R.drawable.document_svgrepo_com,
                    "Journal", active == "Journal")
                Spacer(Modifier.width(16.dp))
                ItemsCard(Modifier.weight(1f).clickable { active="Alerts" }, R.drawable.notification_bell_svgrepo_com,
                    "Alerts", active == "Alerts")
            }
        }
    }

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Card(modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(width = 1.dp,
                color = MaterialTheme.colorScheme.surface),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.White), shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)){

            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.rounded_featured_seasonal_and_gifts_24), contentDescription = "",
                        tint = Color(0xFFEE6914))
                    Spacer(Modifier.width(8.dp))
                    Text("Gift Wish for Sarah Johnathan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground)
                }
                Spacer(Modifier.height(16.dp))
                wishlist.forEach{ item ->
                    GiftItem(item)
                    Spacer(Modifier.height(16.dp))
                }
            }

        }
    }
}

@Composable
fun ItemsCard(modifier: Modifier, icon: Int, text: String, current: Boolean){

    Column(modifier.fillMaxWidth().background(color = if(current)MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent,
        shape = RoundedCornerShape(8.dp)
        ).padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(painter = painterResource(id = icon), contentDescription = null, tint = Color.Unspecified)
        Text(text, color = MaterialTheme.colorScheme.onBackground)
    }
}


@Composable
fun GiftItem(item: String){

    val selected = rememberSaveable() { mutableStateOf(false) }

    Column(
        modifier = Modifier.background(color = if(selected.value) Color(0xFFEE6914).copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f),
            shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = if(selected.value) Color(0xFFEE6914).copy(alpha = 0.4f) else Color.Gray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(8.dp))
            .padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Row {
                Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
                Spacer(Modifier.width(16.dp))
                Text(item, color = MaterialTheme.colorScheme.onBackground)
            }
            IconButton(onClick = {selected.value = !selected.value}){
                Icon(imageVector = Icons.Filled.Star, contentDescription = null, tint = if(selected.value) Color(0xFFEE6914) else Color.Gray)
            }

        }
    }
}
