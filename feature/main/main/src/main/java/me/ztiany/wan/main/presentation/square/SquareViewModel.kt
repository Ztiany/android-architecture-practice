package me.ztiany.wan.main.presentation.square

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.base.fragment.ui.Paging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import me.ztiany.wan.main.data.HomeRepository
import me.ztiany.wan.main.presentation.feed.ArticleVOMapper
import javax.inject.Inject

@HiltViewModel
class SquareViewModel @Inject constructor(
    repository: HomeRepository,
    articleVOMapper: ArticleVOMapper,
) : ViewModel() {

    val squareFlow = repository.loadSquareArticles(
        Paging.defaultPagingStart,
        Paging.defaultPagingSize
    ).cachedIn(viewModelScope)
        .map {
            it.map(articleVOMapper::mapArticle)
        }

}