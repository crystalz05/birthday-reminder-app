package com.tyro.birthdayreminder.ui.screen.home_screen_items

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.custom_class.getMonth
import com.tyro.birthdayreminder.navigation.Screen
import com.tyro.birthdayreminder.view_model.BirthdayContactViewModel
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OptionsCard(
    navHostController: NavHostController,
    onclick:()->Unit,
    birthdayContactViewModel: BirthdayContactViewModel
    ){

    val contacts by birthdayContactViewModel.contacts.collectAsState()

    val thisMonthBirthdays = contacts.filter { contact -> getMonth(contact.birthday) == LocalDate.now().monthValue }

    Card(modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color.White), shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)){
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = {navHostController.navigate(Screen.AddBirthDay.route)}, modifier = Modifier.background(shape = CircleShape,
                        color = Color(0xFFF44336)
                    )) {
                        Icon(painterResource(id = R.drawable.round_person_add_alt_24), contentDescription = "")
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Add Contact", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = {}, modifier = Modifier.background(shape = CircleShape,
                        color = Color(0xFFFF9C07)
                    )) {
                        Icon(painterResource(id = R.drawable.baseline_search_24), contentDescription = "")
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Find Birthday", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = {onclick()}, modifier = Modifier.background(shape = CircleShape,
                        color = Color(0xFF9C27B0)
                    )) {
                        Icon(Icons.Default.DateRange, contentDescription = "")
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("View Calendar", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = {navHostController.navigate(Screen.Stats.route)}, modifier = Modifier.background(shape = CircleShape,
                        color = Color(0xFF2196F3)
                    )) {
                        Icon(painterResource(id = R.drawable.baseline_align_vertical_bottom_24), contentDescription = "")
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Statistics", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
    Spacer(Modifier.height(16.dp))
    Row{
        Card(modifier = Modifier.weight(1f).clickable { navHostController.navigate(Screen.ContactList.route) },
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.White), shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ){
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
                ) {
                Column {
                    Text("Total Contacts",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Normal)
                    Text("${contacts.size}", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                }
                IconButton(onClick = {}, modifier = Modifier.background(shape = CircleShape,
                    color = Color(0xFFF44336)
                )) {
                    Icon(painterResource(id = R.drawable.outline_contacts_24), contentDescription = "")
                }
            }
        }
        Spacer(Modifier.width(16.dp))
        Card(modifier = Modifier.weight(1f),
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.White), shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ){
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("This Month",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Normal)
                    Text("${thisMonthBirthdays.size}", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                }
                IconButton(onClick = {}, modifier = Modifier.background(shape = CircleShape,
                    color = Color(0xFFF44336)
                )) {
                    Icon(Icons.Default.DateRange, contentDescription = "")
                }
            }
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun OptionsCardPreview(){
//    OptionsCard()
//}