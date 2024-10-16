package com.app.sample.compose.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.android.sdk.net.ServiceContext
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.extension.map
import com.app.base.utils.IntKeyPagingSource
import com.app.common.api.dispatcher.DispatcherProvider
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    private val homeApiContext: ServiceContext<WanAndroidApi>,
    private val dispatcherProvider: DispatcherProvider,
) {

    suspend fun fakeQuery(): CallResult<Boolean> {
        return withContext(dispatcherProvider.io()) {
            delay(3000)
            homeApiContext.apiCall {
                loadBanners()
            }
        }.map { true }
    }

    suspend fun fakeUpdate(open: Boolean): CallResult<Boolean> {
        return withContext(dispatcherProvider.io()) {
            delay(3000)
            homeApiContext.apiCall {
                loadBanners()
            }
        }.map { open }
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
                delay(3000)
                if (page >= 3) {
                    throw IOException("Test exception")
                }
                loadSquareArticles(page, size)
            }.datas
        }
    }.flow

    suspend fun loadHomeArticlesCallback(page: Int, pageSize: Int): CallResult<List<Article>> {
        Timber.d("loadHomeArticles: page = $page, pageSize = $pageSize")
        return withContext(dispatcherProvider.io()) {
            delay(3000)
            homeApiContext.apiCall {
                loadHomeArticles(page, pageSize)
            }
        }.map {
            it.datas
        }
    }

}