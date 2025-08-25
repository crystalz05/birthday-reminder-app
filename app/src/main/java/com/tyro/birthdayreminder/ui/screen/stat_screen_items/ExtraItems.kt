package com.tyro.birthdayreminder.ui.screen.stat_screen_items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tyro.birthdayreminder.R

@Composable
fun PercentageBarWithText(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .background(
                    brush = Brush.linearGradient(colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)),
                    shape = RoundedCornerShape(6.dp)
                )
        ){
            if(progress > 0.15f){
                Text(
                    text = "${(progress * 100).toInt()}%",
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.TopEnd).padding(end = 8.dp)
                )
            }
        }
        if(progress <= 0.15f) {
            Text(
                text = "${(progress * 100).toInt()}%", fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun PercentageBar(progress: Float, relationship: String) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.size(10.dp, 10.dp).background(shape = CircleShape, color = MaterialTheme.colorScheme.secondary))
                Text(relationship, color = MaterialTheme.colorScheme.secondary)
            }
            Text(
                text = "23 (${(progress * 100).toInt()}%)",
                color = MaterialTheme.colorScheme.secondary,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        ) {
            Text("Friends", color = MaterialTheme.colorScheme.primary)
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .background( color = colorResource(id = R.color.cyan))
            )
        }

    }
}

@Composable
fun UpcomingBirthdayItem(){
    Column(modifier = Modifier
        .background(color = colorResource(id = R.color.teal_200).copy(0.1f), shape = RoundedCornerShape(8.dp))
        .border(width = 1.dp, color = colorResource(id = R.color.teal_200).copy(0.5f), shape = RoundedCornerShape(8.dp))
        .padding(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column() {
                Text("Emma Wilson", style = MaterialTheme.typography.titleMedium)
                Text("Tomorrow", style = MaterialTheme.typography.titleSmall)
            }
            Column(modifier = Modifier
                .background(color = colorResource(id = R.color.orange).copy(0.2f), shape = RoundedCornerShape(24.dp))
                .border(width = 1.dp, color = colorResource(id = R.color.orange).copy(0.8f), shape = RoundedCornerShape(24.dp))
                .padding(10.dp, 3.dp)
            ) {
                Text("Tomorrow", style = MaterialTheme.typography.labelLarge, color = colorResource(id = R.color.deep_orange))
            }
        }
    }
}

@Composable
fun PercentageBarWithTextForAgeDistribution(progress: Float) {
    Row {
        Text("16-25", modifier = Modifier.width(50.dp), color = MaterialTheme.colorScheme.onBackground)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.linearGradient(colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)),
                        shape = RoundedCornerShape(6.dp)
                    )
            ){
                if(progress > 0.15f){
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.align(Alignment.TopEnd).padding(end = 8.dp)
                    )
                }
            }
            if(progress <= 0.15f) {
                Text(
                    text = "${(progress * 100).toInt()}%", fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Text("18%", modifier = Modifier.width(50.dp), textAlign = TextAlign.Right, color = MaterialTheme.colorScheme.onBackground)
    }
}