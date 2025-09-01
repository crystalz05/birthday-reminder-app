package com.tyro.birthdayreminder.ui.screen.scaffold_contents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ScaffoldTitle(text: String){
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge)
    }
}


@Composable
fun ScaffoldNavigation(navHostController: NavHostController){
    IconButton(onClick = {navHostController.navigateUp()},
        modifier = Modifier.padding(start = 10.dp, end = 20.dp)
    ){
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}


@Composable
fun ScaffoldAction(){
    Icon(imageVector = Icons.Default.Notifications,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onBackground,
    )
    Spacer(Modifier.width(8.dp))
}