package com.app.sample.compose.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.app.sample.compose.vo.ArticleMapper
import com.app.sample.compose.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class Paging3ViewModel @Inject constructor(
    repository: Repository,
    articleMapper: ArticleMapper,
) : ViewModel() {

    val squareFlow = repository.loadSquareArticles(
        0,
        20
    )
        .map { it.map(articleMapper::mapArticle) }
        .cachedIn(viewModelScope)
}