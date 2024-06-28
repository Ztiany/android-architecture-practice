package me.ztiany.wan.sample.data

import com.android.sdk.cache.getEntity
import com.android.sdk.net.ServiceContext
import com.app.base.data.storage.StorageManager
import com.app.common.api.dispatcher.DispatcherProvider
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@ActivityRetainedScoped
class MVISampleRepository @Inject constructor(
    private val homeApiContext: ServiceContext<WanAndroidApi>,
    private val dispatcherProvider: DispatcherProvider,
    private val storageManager: StorageManager
) {

    private val localArticleFlow = flow {
        emit(
            LoadedData(
                storageManager.stable().getEntity<List<Article>>(ARTICLE_CACHE_KEY) ?: emptyList(),
                false
            )
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun firstArticlePage(pageStart: Int, pageSize: Int) = flowOf(
        localArticleFlow,
        loadFirstArticlePage(pageStart, pageSize)
    )
        .flattenMerge()
        .transformWhile {
            Timber.d("Init.toPartialChangeFlow fromRemote=${it.fromRemote}")
            emit(it.data)
            !it.fromRemote // return true to continue, false to stop.
        }

    private fun loadFirstArticlePage(pageStart: Int, pageSize: Int) = suspend {
        Timber.d("loadHomeArticles pageNo=$pageStart pageSize=$pageSize")
        delay(1000)
        homeApiContext.executeApiCall { loadHomeArticles(pageStart, pageSize) }
    }
        .asFlow()
        .map { pager ->
            if (pager.datas.isNotEmpty()) {
                storageManager.stable().putEntity(ARTICLE_CACHE_KEY, pager.datas)
                LoadedData(pager.datas, true /* 网络请求成功时，中断流 */)
            } else {
                LoadedData(emptyList(), false /* 没有数据，则用空数据替代 */)
            }
        }

    suspend fun loadMoreArticles(pageNo: Int, pageSize: Int): List<Article> {
        Timber.d("loadHomeArticles pageNo=$pageNo pageSize=$pageSize")
        return withContext(dispatcherProvider.io()) {
            homeApiContext.executeApiCall {
                loadHomeArticles(pageNo, pageSize)
            }
        }.datas
    }

    fun reportArticle(id: Int) =
        suspend {
            delay(2000)
            id
        }.asFlow()

    companion object {
        const val ARTICLE_CACHE_KEY = "article_cache_key"
    }

}