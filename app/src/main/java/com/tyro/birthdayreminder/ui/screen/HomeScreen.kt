package com.tyro.birthdayreminder.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tyro.birthdayreminder.screen.home_screen_items.OptionsCard
import com.tyro.birthdayreminder.screen.home_screen_items.TodayBirthday
import com.tyro.birthdayreminder.screen.home_screen_items.UpComingBirthdays
import com.tyro.birthdayreminder.ui.theme.BirthdayReminderTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    var currentTab by remember { mutableStateOf("Home") }

    val tabs = listOf(
        "Home" to Icons.Outlined.Home,
        "Calendar" to Icons.Outlined.DateRange,
        "Contact" to Icons.Outlined.AccountBox,
        "Profile" to Icons.Outlined.Person
    )
    val selectedIndex  = tabs.indexOfFirst { it.first == currentTab }

    Scaffold(
        topBar = {
            TopAppBar(
                expandedHeight = 70.dp,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background, titleContentColor = Color.White),
                title ={Text("", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp))},
                navigationIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
                        Box(modifier = Modifier
                            .size(50.dp, 50.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                shape = CircleShape
                            )){
                            Icon(modifier = Modifier.fillMaxSize(), tint = Color.Gray,
                                imageVector = Icons.Filled.AccountCircle, contentDescription = "")
                        }
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text("Good Morning", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            Text("Paul Michael", style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Medium)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Outlined.Search, contentDescription = "")
                    }
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "")
                    }
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Outlined.Settings, contentDescription = "")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}, shape = CircleShape, containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)) {
                Icon(Icons.Default.Add, contentDescription = "")

            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.wrapContentHeight()
                ){

                TabRow(selectedTabIndex = selectedIndex) {
                    tabs.forEachIndexed {
                        index, (title, icon)->
                        Tab(
                         selected = selectedIndex == index,
                            onClick = {currentTab = title},
                            text = { Text(title) },
                            icon = {Icon(icon, contentDescription = null)}
                        )
                    }
                }
//                Row(modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 4.dp)
//                    .fillMaxHeight()){
//                    Box(modifier = Modifier
//                        .weight(1f)
//                        .fillMaxHeight(), Alignment.Center){
//                        BottomAppBarItem(icon = Icons.Outlined.Home, text = "Home", isSelected = selectedItem == "Home", onClick = {selectedItem = "Home"})
//                    }
//                    Box(modifier = Modifier
//                        .weight(1f)
//                        .fillMaxHeight(), Alignment.Center){
//                        BottomAppBarItem(icon = Icons.Outlined.DateRange, text = "Calender", isSelected = selectedItem == "Calendar", onClick = {selectedItem = "Calendar"})
//                    }
//                    Box(modifier = Modifier
//                        .weight(1f)
//                        .fillMaxHeight(), Alignment.Center){
//                        BottomAppBarItem(icon = Icons.Outlined.AccountBox, text = "Contact", isSelected = selectedItem == "Contact", onClick = {selectedItem = "Contact"})
//                    }
//                    Box(modifier = Modifier
//                        .weight(1f)
//                        .fillMaxHeight(), Alignment.Center){
//                        BottomAppBarItem(icon = Icons.Outlined.Person, text = "Profile", isSelected = selectedItem == "Profile", onClick = {selectedItem = "Profile"})
//                    }
//                }
            }
        },
        content = { innerPadding ->
            LazyColumn(Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)) {

                item{
                    Spacer(Modifier.height(8.dp))
                    TodayBirthday()
                    Spacer(Modifier.height(16.dp))
                    OptionsCard()
                    Spacer(Modifier.height(16.dp))
                    UpComingBirthdays()
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    )

}

@Composable
fun BottomAppBarItem(
    icon: ImageVector,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
)
{
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else Color.Transparent
    val interactionSource = remember { MutableInteractionSource() }
    val contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
    val borderColor = if(isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent


    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(10.dp))
            .background(
                shape = RoundedCornerShape(10.dp),
                color = backgroundColor
            )
            .size(80.dp)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }) {
        Icon(icon, contentDescription = "", tint = contentColor)
        Text(text, color = contentColor)
        if(isSelected) Text("•", color = contentColor)
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    BirthdayReminderTheme {
        HomeScreen()
    }
}