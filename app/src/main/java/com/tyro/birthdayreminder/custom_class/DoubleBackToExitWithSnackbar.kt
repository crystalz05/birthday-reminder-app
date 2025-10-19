package com.tyro.birthdayreminder.custom_class

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DoubleBackToExitWithSnackbar(snackbarHostState: SnackbarHostState) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var backPressedOnce by remember { mutableStateOf(false) }

    BackHandler {
        if (backPressedOnce) {
            (context as? Activity)?.finish()
        } else {
            backPressedOnce = true
            scope.launch {
                snackbarHostState.showSnackbar("Swipe again to exit")
                delay(2000)
                backPressedOnce = false
            }
        }
    }
}

@Composable
fun DoubleBackToExitHandler(
    onExit: () -> Unit,
    message: String = "Press again to exit",
    delayMillis: Long = 2000
) {
    var backPressedOnce by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler {
        if (backPressedOnce) {
            onExit()
        } else {
            backPressedOnce = true
            scope.launch {
                snackbarHostState.showSnackbar(message)
                delay(delayMillis)
                backPressedOnce = false
            }
        }
    }

    SnackbarHost(hostState = snackbarHostState)
}