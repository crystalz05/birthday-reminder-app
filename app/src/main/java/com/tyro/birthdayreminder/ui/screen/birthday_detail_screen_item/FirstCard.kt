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
import com.tyro.birthdayreminder.custom_class.getZodiacSign
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FirstCard(monthLeft: Int?, dayLeft: Int?, birthMonth:Int?, birthDay:Int?, dayOfWeek:String?, currentAge:Int?, turningAge:Int?, birthDayDateIntValue: LocalDate?) {

    val zodiacSign = birthDayDateIntValue?.let { getZodiacSign(it) }

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

    val monthText = birthMonth?.let { Month.of(it).getDisplayName(TextStyle.FULL, Locale.getDefault()) }

    @Composable
    fun RemainingTime(month: Int, day: Int) {
        Text(
            buildAnnotatedString {
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
                            if (monthLeft != null) {
                                if (dayLeft != null) {
                                    RemainingTime(monthLeft, dayLeft)
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("until next birthday", color = MaterialTheme.colorScheme.onSurface,
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
                                Text("$monthText $birthDay", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
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
                                    Text(zodiacSigns[zodiacSign]?:"", style = MaterialTheme.typography.headlineMedium)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(zodiacSign ?: "Unknown", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                                    Text(zodiacTraits[zodiacSign]?: "", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
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