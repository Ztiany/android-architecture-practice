package me.ztiany.wan.main.home.feed.data

import com.android.sdk.net.ServiceContext
import com.app.common.api.dispatcher.DispatcherProvider
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class FeedRepository @Inject constructor(
    private val homeApiContext: ServiceContext<FeedApi>,
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

}