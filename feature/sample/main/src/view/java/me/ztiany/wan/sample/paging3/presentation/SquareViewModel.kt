package me.ztiany.wan.sample.paging3.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.base.fragment.ui.Paging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import me.ztiany.wan.sample.epoxy.ArticleMapper
import me.ztiany.wan.sample.paging3.data.Paging3SampleRepository
import javax.inject.Inject

@HiltViewModel
class SquareViewModel @Inject constructor(
    repository: Paging3SampleRepository,
    articleMapper: ArticleMapper,
) : ViewModel() {

    val squareFlow = repository.loadSquareArticles(
        Paging.defaultPagingStart,
        Paging.defaultPagingSize
    ).map {
        it.map(articleMapper::mapArticle)
    }.cachedIn(viewModelScope)

}