package com.tyro.birthdayreminder.ui.screen.profile_settings_screen_components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tyro.birthdayreminder.R
import com.tyro.birthdayreminder.navigation.Screen

@Composable
fun ProfilePhotoSection_2(navHostController: NavHostController){
    Card(modifier = Modifier.shadow(elevation = 2.dp, shape = RoundedCornerShape(6.dp), clip = false)
        .background(color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(6.dp)
        )
        .border(width = 2.dp, color = Color.Transparent, shape = RoundedCornerShape(6.dp))
        .padding(16.dp),
        content = {
            Box(modifier = Modifier.fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.size(110.dp, 110.dp).background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f), shape = CircleShape), Alignment.Center){
                        Box(Modifier.size(100.dp, 100.dp).background(Color.White, shape = CircleShape), Alignment.Center, content = {})
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp, 100.dp)
                                .align(Alignment.Center) // places it at top-right corner of the Box
                                .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape).padding(6.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.BottomEnd) // places it at top-right corner of the Box
                                .background(color = MaterialTheme.colorScheme.primary.copy(0.5f), shape = CircleShape).padding(6.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Paul Michael",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Normal)
                    Text("mikebingp@gmail.com",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                        fontWeight = FontWeight.Normal)

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(onClick = {navHostController.navigate(Screen.AccountVerification.route)}, shape = RoundedCornerShape(4.dp)) {
                        Text("Edit Profile", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    )
    Spacer(Modifier.height(8.dp))
}