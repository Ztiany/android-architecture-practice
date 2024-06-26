package me.ztiany.wan.sample.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.android.sdk.net.ServiceContext
import com.app.common.api.dispatcher.DispatcherProvider
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class SampleRepository @Inject constructor(
    private val homeApiContext: ServiceContext<SampleApi>,
    private val dispatcherProvider: DispatcherProvider,
) {

    suspend fun loadBanner(): List<Banner> {
        return withContext(dispatcherProvider.io()) {
            homeApiContext.executeApiCall {
                loadBanners()
            }
        }
    }

    suspend fun loadTopArticles(): List<Article> {
        return withContext(dispatcherProvider.io()) {
            homeApiContext.executeApiCall {
                loadTopArticles()
            }
        }
    }

    suspend fun loadArticles(page: Int, pageSize: Int): List<Article> {
        return withContext(dispatcherProvider.io()) {
            homeApiContext.executeApiCall {
                loadHomeArticles(page, pageSize)
            }
        }.datas
    }

    fun loadSquareArticles(pageStart: Int, pageSize: Int) = Pager(
        PagingConfig(
            pageSize = pageSize,
            initialLoadSize = pageSize,
            enablePlaceholders = false
        )
    ) {
        IntKeyPagingSource(
            pageStart = pageStart,
            serviceContext = homeApiContext
        ) { serviceContext, page, size ->
            serviceContext.executeApiCall {
                loadSquareArticles(page, size)
            }.datas
        }
    }.flow


    val localNewsFlow = flow {
        //val news = newsDao.queryNewsSuspend()
        //val newsList = news.map { it.convert() }
        // 使用 NewsFlowWrapper 包装数据库流
        emit(ArticleFlowWrapper(emptyList(), false))
    }

    val localNewsOneShotFlow = flow {
        //val news = newsDao.queryNewsSuspend()
        //val newsList = news.map { News(it.path, it.image, it.title, it.passtime,0L) }
        //Log.v("ttaylor", "[collect news] local one shot flow =${news.size}")
        delay(1000)
        //emit(ArticleFlowWrapper(newsList, false))
        emit(ArticleFlowWrapper(emptyList(), false))
    }

    fun remoteNewsFlow(page: Int, count: Int) =
        suspend {
            homeApiContext.executeApiCall {
                loadHomeArticles(page, count)
            }
        }
            .asFlow()
            .map { pager ->
                if (pager.datas.isNotEmpty()) {
                    //newsDao.deleteAllNews()
                    //newsDao.insertAll(newsBean.result.map { it.toNews() })
                    // 网络请求成功时，中断流
                    ArticleFlowWrapper(pager.datas, true)
                } else {
                    ArticleFlowWrapper(emptyList(), false)
                }
            }

    fun reportNews(id: Int) =
        suspend {
            delay(1000)
            id
        }.asFlow()

}