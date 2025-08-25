package com.tyro.birthdayreminder.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tyro.birthdayreminder.screen.birthday_detail_screen_item.FirstCard
import com.tyro.birthdayreminder.screen.birthday_detail_screen_item.SecondCard
import com.tyro.birthdayreminder.screen.birthday_detail_screen_item.ThirdCard
import com.tyro.birthdayreminder.ui.theme.BirthdayReminderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayDetailScreen() {

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                TopAppBar(
                    title = { Text("Birthday Details", style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground) },                    navigationIcon = {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(start = 10.dp, end = 20.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        }

    ) {innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                Column(modifier = Modifier.padding(bottom = 20.dp).fillMaxWidth().background(MaterialTheme.colorScheme.surface), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.size(100.dp, 100.dp).background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), shape = CircleShape), Alignment.Center){
                        Box(Modifier.size(90.dp, 100.dp).background(Color.White, shape = CircleShape), Alignment.Center, content = {})
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Sarah Johnson",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Row {
                        Text("Best Friend", style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold, color= MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(16.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Turning 29", style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold, color= MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(16.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp))
                    }
                    Spacer(Modifier.height(20.dp))
                }
            }
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    FirstCard()
                    SecondCard()
                    ThirdCard()
                }

            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun BirthdayDetailScreenPreview(){
    BirthdayReminderTheme{
        BirthdayDetailScreen()
    }
}