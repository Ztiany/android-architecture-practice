package me.ztiany.wan.main.home.feed

import androidx.lifecycle.ViewModel
import com.android.base.fragment.epoxy.ListState
import com.android.base.fragment.epoxy.appendList
import com.android.base.fragment.epoxy.replaceList
import com.android.base.fragment.epoxy.toLoadMoreError
import com.android.base.fragment.epoxy.toLoadingMore
import com.android.base.fragment.epoxy.toRefreshError
import com.android.base.fragment.epoxy.toRefreshing
import com.android.base.fragment.ui.AutoPaging
import com.android.base.fragment.vm.startListJob
import com.app.base.utils.rethrowIfCancellation
import me.ztiany.wan.main.home.data.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * @author Ztiany
 */
@HiltViewModel
class FeedViewModule @Inject constructor(
    private val homeRepository: HomeRepository,
    private val articleVOMapper: ArticleVOMapper,
) : ViewModel() {

    private val _articles = MutableStateFlow<ListState<FeedItem>>(ListState())
    val articles = _articles.asStateFlow()

    private var paging = AutoPaging {
        articles.value.data.filter { it is ArticleVO && !it.isTop }.size
    }

    init {
        refresh()
    }

    fun refresh() = startListJob {
        _articles.update { it.toRefreshing() }

        try {
            val bannerList = homeRepository.loadBanner()
            val topArticles = homeRepository.loadTopArticles()
            val articles = homeRepository.loadArticles(paging.start, 20)

            buildList {
                add(articleVOMapper.mapBanner(bannerList))
                addAll(articleVOMapper.mapArticle(topArticles))
                addAll(articleVOMapper.mapArticle(articles))
            }.apply {
                _articles.update { it.replaceList(this, paging.hasMore(articles.size)) }
            }

        } catch (e: Exception) {
            e.rethrowIfCancellation()
            _articles.update { it.toRefreshError(e) }
        }
    }

    fun loadMore() = startListJob {
        _articles.update { it.toLoadingMore() }
        try {
            val articles = homeRepository.loadArticles(paging.next, paging.total)
            articleVOMapper.mapArticle(articles).apply {
                _articles.update { it.appendList(this, paging.hasMore(articles.size)) }
            }
        } catch (e: Exception) {
            e.rethrowIfCancellation()
            _articles.update { it.toLoadMoreError(e) }
        }
    }

}
