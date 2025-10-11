package com.tyro.birthdayreminder.ui.screen.home_screen_items

import android.os.Build
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tyro.birthdayreminder.view_model.BirthdayContactViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchInterface(
    isSearchVisible: Boolean, openSearch: ()-> Unit,
    closeSearch: ()-> Unit,
    navHostController: NavHostController,
    birthdayContactViewModel: BirthdayContactViewModel = hiltViewModel()
    ) {

    val contacts by birthdayContactViewModel.contacts.collectAsState()
    var query by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

        // Search overlay
        AnimatedVisibility(
            visible = isSearchVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(modifier = Modifier.fillMaxSize().shadow(elevation = 5.dp)) {
                // Main screen content

                Box(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { focusManager.clearFocus() }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Spacer(Modifier.height(100.dp))

                    OutlinedTextField(
                        shape = RoundedCornerShape(100),
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Search contacts...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp), // reduce height (default ~56.dp)
                        colors = TextFieldDefaults.colors(
//                            unfocusedContainerColor = MaterialTheme.colorScheme.onBackground.copy(0.4f),
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    val results = if(query.isNotBlank()) contacts.filter {
                        it.fullName.contains(query, ignoreCase = true)
                    } else emptyList()

                    LazyColumn {
                        items(results) { contact ->
                            ContactCard(
                                contact,
                                navHostController
                            )
                        }
                    }
                }

            }
        }
    }
}