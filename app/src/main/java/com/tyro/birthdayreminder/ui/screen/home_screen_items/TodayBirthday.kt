package com.tyro.birthdayreminder.ui.screen.home_screen_items

import android.content.ClipData.Item
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tyro.birthdayreminder.R


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodayBirthday(){

    val items = (1..3).toList()
//    val items = emptyList<String>()
4
    LazyColumn(Modifier.heightIn(min = 100.dp, max = 400.dp).fillMaxWidth()
        .background(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF2196F3), // Blue
                    Color(0xFF21CBF3)  // Light Blue
                )
            )
            , shape = RoundedCornerShape(8.dp)).padding(16.dp)) {
        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF21CBF3))
                    .padding(vertical = 16.dp, horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.rounded_featured_seasonal_and_gifts_24),
                            tint = Color.White,
                            contentDescription = ""
                        )
                        Text(
                            "Today's Birthdays",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.White.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                            .size(25.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${items.size}",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        if(items.isEmpty()){
            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
                    Icon(Icons.Outlined.Info, contentDescription = "", tint = Color(0xFFFF9800))
                    Text("No Birthdays today", style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal, color = Color(0xFFFFFFFF)
                    )
                }
            }
        }
        items(items.size) {
            Card(modifier = Modifier.fillMaxWidth()
                .padding(top = if (it == 0) 5.dp else 0.dp), // ✅ Push first item down ,
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.2f),
                    contentColor = Color.White), shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)){
                        Box(modifier = Modifier
                            .size(60.dp)
                            .background(color = Color.White.copy(alpha = 0.2f), shape = CircleShape)){
                            Icon(modifier = Modifier.fillMaxSize(), tint = Color.White,
                                imageVector = Icons.Filled.AccountCircle, contentDescription = "")
                        }
                        Column {
                            Text("Sarah Johnson", fontSize = 20.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold)
                            Text("Turning 29 • Friend",
                                color = Color.White,
                                fontSize = 16.sp, fontWeight = FontWeight.Light)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(modifier = Modifier.weight(1f), onClick = {}, shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(painterResource(id = R.drawable.round_chat_24), contentDescription = "", tint = Color.White)
                                Text("Message",color = Color.White,)
                            }
                        }
                        Spacer(Modifier.width(16.dp))
                        Button(modifier = Modifier.weight(1f), onClick = {}, shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Phone, contentDescription = "", tint = Color.White)
                                Text("Call",color = Color.White,)
                            }
                        }

                    }
                }
            }
            Spacer(Modifier.height(16.dp))

        }

    }

}

@Preview(showBackground = true)
@Composable
fun TodayBirthdayPreview(){
    TodayBirthday()
}