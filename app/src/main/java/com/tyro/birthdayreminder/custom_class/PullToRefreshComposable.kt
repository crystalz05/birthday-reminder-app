package com.tyro.birthdayreminder.custom_class

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyro.birthdayreminder.view_model.BirthdayContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshCustomStyle(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    birthdayContactViewModel: BirthdayContactViewModel = hiltViewModel()
) {
    val state = rememberPullToRefreshState()
    val isRefreshing by birthdayContactViewModel.isRefreshing.collectAsState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { birthdayContactViewModel.loadContacts(showLoading = false, pullDown =  true) },
        modifier = modifier,
        state = state,
        indicator = {
            Indicator(
                modifier = Modifier.padding(top = 100.dp).align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = MaterialTheme.colorScheme.surface,
                color = MaterialTheme.colorScheme.onSurface,
                state = state
            )
        },
    ) {
        content()
    }
}