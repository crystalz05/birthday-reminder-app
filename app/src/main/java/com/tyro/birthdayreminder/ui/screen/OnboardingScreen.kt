package com.tyro.birthdayreminder.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.tyro.birthdayreminder.custom_class.OnboardingPage
import com.tyro.birthdayreminder.custom_class.onboardingPages
import com.tyro.birthdayreminder.data_store.AppSettingsDataStore
import com.tyro.birthdayreminder.view_model.AppSettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    appSettingsViewModel: AppSettingsViewModel,
    onFinish: () -> Unit
){
    val pagerState = rememberPagerState(
        pageCount = { onboardingPages.size }
    )

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title ={Text("Onboarding", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp))},
                actions = {
                    TextButton(onClick = {
                        scope.launch {
                            appSettingsViewModel.setOnboardingCompleted(true)
                            delay(200L)
                            onFinish()
                        }

                    }) {
                        Text("Skip Onboarding >>", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Swipe to Continue",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary.copy(0.8f),
                    textAlign = TextAlign.Center
                )
                Icon(Icons.AutoMirrored.Default.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary.copy(0.8f))
            }
            Column(
                modifier = Modifier.weight(1f)
                    .background(MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                HorizontalPager(state = pagerState) { page ->
                    OnboardingPageContent(page = onboardingPages[page])
                }
            }
            if(pagerState.currentPage == onboardingPages.lastIndex){
                Column(modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.surface), horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = {
                            if (pagerState.currentPage == onboardingPages.lastIndex) {
                                scope.launch {
                                    appSettingsViewModel.setOnboardingCompleted(true)
                                    delay(200L)
                                    onFinish()
                                }

                            } else {
                                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                            }
                        },
                        modifier = Modifier.padding(16.dp).fillMaxWidth()
                    ) {
                        Text("Get Started")
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (page.lottieRes != null) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(page.lottieRes))
            val progress by animateLottieCompositionAsState(
                composition,
                iterations = LottieConstants.IterateForever
            )
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            )
        } else if (page.imageRes != null) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = page.title,
                modifier = Modifier
                    .fillMaxSize().weight(1f)
            )
        }
        Text(page.title, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            page.description,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(0.8f),
            textAlign = TextAlign.Center
        )
    }
}
