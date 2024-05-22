package me.ztiany.wan.main.presentation.feed

import androidx.lifecycle.ViewModel
import com.android.base.fragment.list.epoxy.ListStateHelper
import com.android.base.fragment.vm.startListJob
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.asStateFlow
import me.ztiany.wan.main.data.HomeRepository
import javax.inject.Inject

/**
 * @author Ztiany
 */
@HiltViewModel
class FeedViewModule @Inject constructor(
    private val homeRepository: HomeRepository,
    private val articleVOMapper: ArticleVOMapper,
) : ViewModel() {

    private val stateHelper = ListStateHelper<FeedItem>(
        listSize = { list ->
            list.filter { it is ArticleVO && !it.isTop }.size
        }
    )

    val articles = stateHelper.state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() = startListJob {
        stateHelper.updateToRefreshing()

        try {
            val bannerList = homeRepository.loadBanner()
            val topArticles = homeRepository.loadTopArticles()
            val articles = homeRepository.loadArticles(stateHelper.paging.start, stateHelper.paging.size)
            buildList {
                add(articleVOMapper.mapBanner(bannerList))
                addAll(articleVOMapper.mapArticle(topArticles))
                addAll(articleVOMapper.mapArticle(articles))
            }.apply {
                stateHelper.replaceListAndUpdate(this, stateHelper.paging.hasMore(articles.size))
            }
        } catch (e: Exception) {
            // check <https://stackoverflow.com/questions/76259793/is-it-necceary-to-rethrow-the-cancellationexception-in-kotlin> for more details.
            ensureActive()
            stateHelper.updateToRefreshError(e)
        }
    }

    fun loadMore() = startListJob {
        stateHelper.updateToLoadingMore()

        try {
            val articles = homeRepository.loadArticles(stateHelper.paging.next, stateHelper.paging.size)
            articleVOMapper.mapArticle(articles).apply {
                stateHelper.appendListAndUpdate(this)
            }
        } catch (e: Exception) {
            ensureActive()
            stateHelper.updateToLoadMoreError(e)
        }
    }

}