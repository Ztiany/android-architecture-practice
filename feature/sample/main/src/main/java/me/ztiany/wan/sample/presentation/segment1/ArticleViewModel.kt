package me.ztiany.wan.sample.presentation.segment1

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ztiany.wan.sample.data.SampleRepository
import me.ztiany.wan.sample.presentation.epoxy.ArticleVO
import me.ztiany.wan.sample.presentation.epoxy.ArticleMapper
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repository: SampleRepository,
    private val articleMapper: ArticleMapper,
) : ViewModel() {

    suspend fun loadArticleList(page: Int, size: Int): List<ArticleVO> {
        return repository.loadHomeArticles(page, size).map {
            articleMapper.mapArticle(it)
        }
    }

}