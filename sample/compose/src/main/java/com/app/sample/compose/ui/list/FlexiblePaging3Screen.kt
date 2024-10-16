package com.app.sample.compose.ui.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.base.compose.architect.list.LoadingItem
import com.app.base.compose.architect.list.StatePagingBox
import com.app.sample.compose.ui.state.ArticleItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlexiblePaging3Screen(
    viewModel: Paging3ViewModel = hiltViewModel(),
) {
    val pagingData = viewModel.squareFlow.collectAsLazyPagingItems()

    StatePagingBox(
        modifier = Modifier.fillMaxSize(),
        lazyPagingItems = pagingData
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(pagingData.itemCount) {
                val item = pagingData[it]
                item?.let { ArticleItem(article = item) }
            }
            LoadingItem(pagingData)
        }
    }
}