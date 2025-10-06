package com.tyro.birthdayreminder.ui.screen.birthday_detail_screen_item

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.custom_class.getAge
import com.tyro.birthdayreminder.custom_class.getDayOfWeek
import com.tyro.birthdayreminder.custom_class.getDaysLeft
import com.tyro.birthdayreminder.custom_class.getMonthAndDay
import com.tyro.birthdayreminder.custom_class.getZodiacSign
import com.tyro.birthdayreminder.view_model.BirthdayContactViewModel
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FirstCard(birthdayContactViewModel: BirthdayContactViewModel) {

    val contact by birthdayContactViewModel.contactDetail.collectAsState()

    val (month, day) = contact?.birthday?.let { getMonthAndDay(it) } ?: (0 to 0)
    val (months, days) = contact?.birthday?.let { getDaysLeft(it) } ?: (0 to 0)

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
    val zodiacTraits = mapOf(
        "Aries" to "Bold • Energetic",
        "Taurus" to "Patient • Reliable",
        "Gemini" to "Curious • Adaptable",
        "Cancer" to "Caring • Intuitive",
        "Leo" to "Confident • Generous",
        "Virgo" to "Practical • Loyal",
        "Libra" to "Fair • Charming",
        "Scorpio" to "Passionate • Determined",
        "Sagittarius" to "Adventurous • Optimistic",
        "Capricorn" to "Disciplined • Ambitious",
        "Aquarius" to "Innovative • Independent",
        "Pisces" to "Compassionate • Artistic"
    )

    val zodiacSign =  when (month) {
            1 -> if (day < 20) "Capricorn" else "Aquarius"
            2 -> if (day < 19) "Aquarius" else "Pisces"
            3 -> if (day < 21) "Pisces" else "Aries"
            4 -> if (day < 20) "Aries" else "Taurus"
            5 -> if (day < 21) "Taurus" else "Gemini"
            6 -> if (day < 21) "Gemini" else "Cancer"
            7 -> if (day < 23) "Cancer" else "Leo"
            8 -> if (day < 23) "Leo" else "Virgo"
            9 -> if (day < 23) "Virgo" else "Libra"
            10 -> if (day < 23) "Libra" else "Scorpio"
            11 -> if (day < 22) "Scorpio" else "Sagittarius"
            12 -> if (day < 22) "Sagittarius" else "Capricorn"
            else -> "Unknown"
    }

    val zodiacElements = mapOf(
        "Aries" to "Fire Sign",
        "Leo" to "Fire Sign",
        "Sagittarius" to "Fire Sign",

        "Taurus" to "Earth Sign",
        "Virgo" to "Earth Sign",
        "Capricorn" to "Earth Sign",

        "Gemini" to "Air Sign",
        "Libra" to "Air Sign",
        "Aquarius" to "Air Sign",

        "Cancer" to "Water Sign",
        "Scorpio" to "Water Sign",
        "Pisces" to "Water Sign"
    )

    val monthText = month.let { Month.of(it).getDisplayName(TextStyle.FULL, Locale.getDefault()) }
    val dayOfWeek = contact?.let { getDayOfWeek(it.birthday) }
    val currentAge = contact?.let { getAge(it.birthday) }
    val turningAge = currentAge?.let { it+1 }

    @Composable
    fun RemainingTime(month: Int, day: Int) {
        Text(
            buildAnnotatedString {
                if(month  <= 0 && day == 1){
                    append("Tomorrow")
                }else if(month <= 0 && day == 0){
                    append("Today")
                }else{
                    if (month > 0) {
                        append("$month ")
                        pushStyle(SpanStyle(fontWeight = FontWeight.Light, fontSize = 18.sp))
                        append("month${if (month > 1) "s" else ""}, ")
                        pop()
                    }
                    append("$day ")
                    pushStyle(SpanStyle(fontWeight = FontWeight.Light, fontSize = 18.sp))
                    append("day${if (day > 1) "s" else ""}")
                    pop()
                }
            },
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 30.dp),
            color = MaterialTheme.colorScheme.tertiary
        )
    }

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

                        Box(modifier = Modifier.background(shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface).height(60.dp), Alignment.Center){
                            RemainingTime(months, days)

                        }
                        Spacer(Modifier.height(8.dp))
                        Text("Next birthday", color = MaterialTheme.colorScheme.onSurface,
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
                                Text("$monthText $day", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                                Text("$dayOfWeek", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
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
                                Text("$turningAge years old", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                                Text("Currently $currentAge", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
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
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                                Column(Modifier.background(color = Color(0xFF8030DA).copy(alpha = 0.5f), shape = CircleShape).padding(4.dp)) {
                                    Text(zodiacSigns[zodiacSign]?: "Unknown", style = MaterialTheme.typography.headlineMedium)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(zodiacSign, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                                    Text(zodiacTraits[zodiacSign]?: "Unknown", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                                }
                                Box(Modifier.border(width = 1.dp, shape = RoundedCornerShape(32.dp), color = Color(0xFF8030DA).copy(alpha = 0.2f)).padding(horizontal = 16.dp, vertical = 4.dp)){
                                    Text(zodiacElements[zodiacSign]?: "Unknown", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }

                    }

                }
            }
        )
    }

}