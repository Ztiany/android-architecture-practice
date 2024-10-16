package com.app.sample.compose.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.base.compose.architect.list.StateLazyColumn
import com.app.sample.compose.ui.state.ArticleItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Paging3Screen(
    viewModel: Paging3ViewModel = hiltViewModel(),
) {
    val pagingData = viewModel.squareFlow.collectAsLazyPagingItems()

    StateLazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        lazyPagingItems = pagingData
    ) {
        items(pagingData.itemCount) {
            val item = pagingData[it]
            item?.let { ArticleItem(article = item) }
        }
    }
}