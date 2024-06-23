package me.ztiany.wan.sample.presentation.segment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ztiany.wan.sample.data.SampleRepository
import me.ztiany.wan.sample.presentation.epoxy.ArticleVO
import me.ztiany.wan.sample.presentation.epoxy.ArticleVOMapper
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repository: SampleRepository,
    private val articleVOMapper: ArticleVOMapper,
) : ViewModel() {

    suspend fun loadArticleList(page: Int, size: Int): List<ArticleVO> {
        return repository.loadArticles(page, size).map {
            articleVOMapper.mapArticle(it)
        }
    }

}