package com.tyro.birthdayreminder.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.auth.ContactOperationState
import com.tyro.birthdayreminder.custom_class.Loading
import com.tyro.birthdayreminder.custom_class.PullToRefreshCustomStyle
import com.tyro.birthdayreminder.custom_class.connection_manager.ConnectivityObserver
import com.tyro.birthdayreminder.custom_class.getTimeOfDay
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.ui.screen.home_screen_items.OptionsCard
import com.tyro.birthdayreminder.ui.screen.home_screen_items.TodayBirthday
import com.tyro.birthdayreminder.ui.screen.home_screen_items.UpComingBirthdays
import com.tyro.birthdayreminder.view_model.AuthViewModel
import com.tyro.birthdayreminder.view_model.BirthdayContactViewModel
import com.tyro.birthdayreminder.view_model.ConnectivityViewModel
import kotlinx.coroutines.delay
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    birthdayContactViewModel: BirthdayContactViewModel = hiltViewModel(),
    connectivityViewModel: ConnectivityViewModel = hiltViewModel()
) {
    val contactOperationState by birthdayContactViewModel.contactOperationState.collectAsState()

    var currentTab by remember { mutableStateOf("Home") }

    val imageUrl by authViewModel.imageUrl.collectAsState()
    val fullName by authViewModel.fullName.collectAsState()

    var greeting by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        when(getTimeOfDay()){
            "morning" -> greeting = "Good Morning!"
            "afternoon" -> greeting = "Good Afternoon!"
            else -> greeting = "Good Evening!"
        }
    }

    val connectivity by connectivityViewModel.status.collectAsState()


    val tabs = listOf(
        TabItem("Home", Icons.Outlined.Home, ""),
        TabItem("Contact", Icons.Outlined.AccountBox, ""),
        TabItem("Calendar", Icons.Outlined.DateRange, ""),
        TabItem("Profile", Icons.Outlined.Person, ""),
        )
    val selectedIndex  = tabs.indexOfFirst { it.title == currentTab }
        .takeIf { it != -1 } ?: 0

    val currentYearMonth = YearMonth.now()
    val pagerState = rememberPagerState(
        initialPage = currentYearMonth.year * 12 + (currentYearMonth.monthValue - 1),
        pageCount = { Int.MAX_VALUE } // effectively "infinite" months
    )

    LaunchedEffect(navController.currentBackStackEntry?.destination?.route) {
        if (navController.currentBackStackEntry?.destination?.route == Screen.Home.route) {
            birthdayContactViewModel.loadContacts(showLoading = false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                expandedHeight = 70.dp,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background, titleContentColor = Color.White),
                title ={Text("", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp))},
                navigationIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp).clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {navController.navigate(Screen.ProfileSetting.route)}) {
                        Box(modifier = Modifier
                            .size(50.dp, 50.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                shape = CircleShape
                            )){
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Profile photo",
                                placeholder = painterResource(id = R.drawable.baseline_person_24),
                                error = painterResource(id = R.drawable.baseline_person_24),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize().padding(2.dp)
                                    .align(Alignment.Center)
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(greeting, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            Text(fullName ?: "Unknown", style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Medium)
                            if(connectivity == ConnectivityObserver.Status.Unavailable || connectivity == ConnectivityObserver.Status.Lost ){
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Outlined.Info, modifier = Modifier.size(15.dp), contentDescription = "error", tint = MaterialTheme.colorScheme.error)
                                    Text("Offline, please reconnect",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Outlined.Search, contentDescription = "")
                    }
                    IconButton(onClick = {navController.navigate(Screen.Notification.route)}) {
                        Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "")
                    }
                    IconButton(onClick = {navController.navigate(Screen.ProfileSetting.route)}) {
                        Icon(imageVector = Icons.Outlined.Settings, contentDescription = "")
                    }
                },
            )
        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = {navController.navigate(Screen.AddBirthDay.route)}, shape = CircleShape, containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)) {
//                Icon(Icons.Default.Add, contentDescription = "")
//
//            }
//        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.wrapContentHeight()
                ){

                TabRow(selectedTabIndex = selectedIndex) {
                    tabs.forEachIndexed {
                        index, tab ->
                        Tab(
                         selected = selectedIndex == index,
                            onClick = {currentTab = tab.title},
                            text = { Text(tab.title) },
                            icon = {Icon(tab.icon, contentDescription = null)},
                        )
                    }
                }
            }
        },
        content = { innerPadding ->

            Box(modifier = Modifier.fillMaxSize(), Alignment.Center){
                when(currentTab){
                    "Home" ->
                        PullToRefreshCustomStyle(content = {
                            LazyColumn(Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.background)
                                .padding(innerPadding)
                                .padding(horizontal = 16.dp)) {

                                item{
                                    Spacer(Modifier.height(8.dp))
                                    TodayBirthday(navController, birthdayContactViewModel)
                                    Spacer(Modifier.height(16.dp))
                                    OptionsCard(
                                        navController,
                                        onclick = {currentTab = "Calendar"},
                                        birthdayContactViewModel
                                    )
                                    Spacer(Modifier.height(16.dp))
                                    UpComingBirthdays(navController, birthdayContactViewModel)
                                    Spacer(Modifier.height(8.dp))
                                }
                            }
                        })
                    "Calendar"->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            HorizontalPager(state = pagerState) { page ->
                                val year = page / 12
                                val month = (page % 12) + 1
                                CalendarMonth(year, month)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Special notes", modifier = Modifier.padding(start = 16.dp))
                            }
                        }
                    "Contact"->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            ContactScreen()
                        }
                    "Profile"->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            ProfileScreen(navController, authViewModel)
                        }
                }

                if (contactOperationState is ContactOperationState.Loading) {
                    Loading(
                        retry = {
                            birthdayContactViewModel.loadContacts(showLoading = true)
                        }
                    )
                }
            }
        }
    )
}

data class TabItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

//
//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview(){
//    BirthdayReminderTheme {
//        HomeScreen()
//    }
//}