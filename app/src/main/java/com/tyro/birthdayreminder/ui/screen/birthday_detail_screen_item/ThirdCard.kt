package com.tyro.birthdayreminder.ui.screen.birthday_detail_screen_item

import android.os.Build
import android.util.Log
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.custom_class.Loading
import com.tyro.birthdayreminder.custom_class.getAge
import com.tyro.birthdayreminder.entity.Contact
import com.tyro.birthdayreminder.entity.WishItem
import com.tyro.birthdayreminder.view_model.BirthdayContactViewModel
import com.tyro.birthdayreminder.view_model.ContactFormViewModel
import com.tyro.birthdayreminder.view_model.GeminiViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ThirdCard(
    contactId: String,
    onSectionSelected: (String) -> Unit = {},
    birthdayContactViewModel: BirthdayContactViewModel,
    contactFormViewModel: ContactFormViewModel = hiltViewModel(),
    geminiViewModel: GeminiViewModel = hiltViewModel()
){

    val contact by birthdayContactViewModel.contactDetail.collectAsState()

    LaunchedEffect(Unit){
        if (contact != null) {
            contact?.let { contactFormViewModel.setFromContact(it) }
            contact?.wishList?.let { birthdayContactViewModel.setGiftItems(it) }
        }
    }

    val giftItems by birthdayContactViewModel.giftItems.collectAsState()
    val giftIsLoading by geminiViewModel.isLoading.collectAsState()

    var active by remember { mutableStateOf("") }

    LaunchedEffect(active) {
        if (active.isNotEmpty()) {
            onSectionSelected(active)
        }
    }

    val formState by contactFormViewModel.formState.collectAsState()

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Card(modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(width = 1.dp,
                color = MaterialTheme.colorScheme.surface),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.White), shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)){


            Row(Modifier.fillMaxWidth().padding(16.dp)) {
                ItemsCard(Modifier.weight(1f).clickable { active="wish_list" }, R.drawable.gift_box_svgrepo_com,
                    "Wish List", active == "wish_list")
                Spacer(Modifier.width(16.dp))
                ItemsCard(Modifier.weight(1f).clickable { active="journal" }, R.drawable.document_svgrepo_com,
                    "Journal", active == "journal")
                Spacer(Modifier.width(16.dp))
            }
        }
    }


    when(active){
        "wish_list" ->{


            Column(modifier = Modifier.padding(vertical = 16.dp)) {

                Card(modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(width = 1.dp,
                        color = MaterialTheme.colorScheme.surface),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = Color.White), shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)){

                    Box(contentAlignment = Alignment.Center) {
                        if(giftIsLoading){
                            Loading()
                        }
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(painter = painterResource(id = R.drawable.rounded_featured_seasonal_and_gifts_24), contentDescription = "",
                                    tint = Color(0xFFEE6914))
                                Spacer(Modifier.width(8.dp))
                                Text("Gift Suggestions for ${contact?.fullName}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onBackground)
                            }
                            Spacer(Modifier.height(16.dp))
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.heightIn(max = 300.dp).overscroll(
                                    rememberOverscrollEffect()
                                )
                            ) {
                                itemsIndexed(giftItems){ index, item ->
                                    GiftItem(item, onToggle = {
                                        val updatedWishList = contactFormViewModel.toggleWishItem(index)
                                        birthdayContactViewModel.updateWishList(contactId, updatedWishList)
                                        birthdayContactViewModel.setGiftItems(updatedWishList)
                                    })
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                contact?.let {
                                    geminiViewModel.fetchWishes(it.fullName, getAge(it.birthday), it.journal?:"") { wishes ->
                                        birthdayContactViewModel.updateWishList(contactId, wishes)
                                        birthdayContactViewModel.setGiftItems(wishes) // update local state too
                                    }
                                }
                            },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()) {
                                Text(if(giftItems.isEmpty())"Get 5 AI Suggestions" else "None good enough? get others." , color = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }
                }
            }
        }
        "journal" -> {

            var editJornal by remember { mutableStateOf(true) }

            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Card(modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(width = 1.dp,
                        color = MaterialTheme.colorScheme.surface),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = Color.White), shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)){

                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(painter = painterResource(id = R.drawable.outline_book_ribbon_24), contentDescription = "",
                                tint = Color(0xFFEE6914))
                            Spacer(Modifier.width(8.dp))
                            Text("Personal Journal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground)
                        }
                        Spacer(Modifier.height(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(Color(0x1E661BD2), Color(0x1B21CBF3)) // blue gradient
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            TextField(
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                                value = formState.journal, onValueChange = {contactFormViewModel.onJournalChange(it)},
                                modifier = Modifier.fillMaxWidth().border(width = 0.4.dp,
                                    color = if(!editJornal) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                                maxLines = 8,
                                readOnly = editJornal
                            )
                            HorizontalDivider(thickness = 0.3.dp, modifier = Modifier.padding(horizontal = 20.dp), color = MaterialTheme.colorScheme.onBackground.copy(0.5f))
                            Spacer(Modifier.height(8.dp))
                            when(editJornal){
                                false -> {
                                    Column {
                                        Row {
                                            Button(
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(8.dp),
                                                onClick = {
                                                    editJornal = !editJornal
                                                    if (contactId != null) {
                                                        birthdayContactViewModel.updateContact(contactId, formState, true)
                                                    }
                                                }) {
                                                Icon(painter = painterResource(id = R.drawable.sharp_save_as_24), contentDescription = "")
                                                Spacer(Modifier.width(8.dp))
                                                Text("Save Journal", style = MaterialTheme.typography.titleMedium)
                                            }
                                            Spacer(Modifier.width(8.dp))
                                            OutlinedButton(
                                                shape = RoundedCornerShape(8.dp),
                                                onClick = {editJornal = !editJornal}) {
                                                Text("Cancel")
                                            }
                                        }
                                    }
                                }
                                true ->{
                                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                                        TextButton(onClick = {editJornal = !editJornal}, ) {
                                            Row {
                                                Icon(Icons.Default.Create, contentDescription = null)
                                                Spacer(Modifier.width(8.dp))
                                                Text("Edit Journal", style = MaterialTheme.typography.titleMedium)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
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
fun GiftItem(item: WishItem, onToggle: () -> Unit){

    Column(
        modifier = Modifier.background(color = if(item.selected) Color(0xFFEE6914).copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f),
            shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = if(item.selected) Color(0xFFEE6914).copy(alpha = 0.4f) else Color.Gray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(8.dp))
            .padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
                Spacer(Modifier.width(16.dp))
                Text(item.name, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f))
                IconButton(onClick = onToggle, modifier = Modifier){
                    Icon(imageVector = Icons.Filled.Star, contentDescription = null, tint = if(item.selected) Color(0xFFEE6914) else Color.Gray)
                }
            }
        }
    }
}