package com.tyro.birthdayreminder.ui.screen

import android.icu.util.Calendar.DAY_OF_WEEK
import android.icu.util.Calendar.DAY_OF_YEAR
import android.icu.util.Calendar.WeekData
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tyro.birthdayreminder.custom_class.TitleCase
import com.tyro.birthdayreminder.ui.screen.scaffold_contents.ScaffoldAction
import com.tyro.birthdayreminder.ui.screen.scaffold_contents.ScaffoldNavigation
import com.tyro.birthdayreminder.ui.screen.scaffold_contents.ScaffoldTitle
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.MonthDay
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarMonth(year: Int, month: Int) {
    val yearMonth = remember { YearMonth.of(year, month) }
    val daysInMonth = yearMonth.lengthOfMonth()


    val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek.value

    val totalCells = firstDayOfWeek - 1 + daysInMonth

    val currentMonth = yearMonth.month.toString()
    val currentYear = yearMonth.year.toString()
    val currentDay = MonthDay.now().dayOfMonth.toString()

    fun isToday(day: Int): Boolean {
        val now = YearMonth.now()
        return month == now.monthValue && day == LocalDate.now().dayOfMonth
    }

    Column(Modifier.padding(16.dp)) {
        Text(TitleCase(currentMonth), fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp))
        Text(currentYear, fontWeight = FontWeight.Normal,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp))

        Spacer(Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7), // 7 days a week
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(7){day ->
                val dayOfWeek = DayOfWeek.of(day + 1)

                Box(contentAlignment = Alignment.Center
                ){
                    Text(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge
                        )
                }
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(7), // 7 days a week
            modifier = Modifier.fillMaxWidth(),
            content = {
                items(totalCells) { day ->

                    if (day < firstDayOfWeek - 1) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(40.dp)
                        )
                    } else {
                        val dayNumber = day - (firstDayOfWeek - 1) + 1
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(40.dp)
                                .clickable { /* handle day click */ }
                                .background(
                                    color = if (isToday(dayNumber))
                                        MaterialTheme.colorScheme.tertiaryContainer
                                    else Color.Transparent,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayNumber.toString(),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    val dayNumber = day - (firstDayOfWeek - 1) + 1
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(40.dp)
                            .clickable { /* handle day click */ }
                            .background(color = if(isToday(dayNumber)) MaterialTheme.colorScheme.tertiaryContainer else Color.Transparent, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = dayNumber.toString(), color = if(day < firstDayOfWeek-1) Color.Transparent else MaterialTheme.colorScheme.onBackground)
                    }
                }
            }
        )
        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

    }
}