package me.ztiany.wan.sample.presentation.paging3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.base.fragment.ui.Paging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import me.ztiany.wan.sample.data.SampleRepository
import me.ztiany.wan.sample.presentation.epoxy.ArticleVOMapper
import javax.inject.Inject

@HiltViewModel
class SquareViewModel @Inject constructor(
    repository: SampleRepository,
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