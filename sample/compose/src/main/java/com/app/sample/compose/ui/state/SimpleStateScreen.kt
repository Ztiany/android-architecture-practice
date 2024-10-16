package com.app.sample.compose.ui.state

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.base.compose.architect.state.StateBox


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleStateScreen(
    viewModel: RefreshStateViewModel = hiltViewModel(),
) {
    val articleState = viewModel.articles.collectAsStateWithLifecycle()
    StateBox(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        pageState = articleState,
        pullToRefreshState = rememberPullToRefreshState(enabled = { false }),
        onRefresh = { viewModel.refresh() }
    ) {
        ArticleList(it)
    }
}