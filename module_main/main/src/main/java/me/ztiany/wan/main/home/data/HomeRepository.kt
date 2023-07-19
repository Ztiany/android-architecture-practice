package me.ztiany.wan.main.home.data

import com.android.sdk.net.ServiceContext
import com.app.base.app.DispatcherProvider
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class HomeRepository @Inject constructor(
    private val homeApiContext: ServiceContext<HomeApi>,
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
                loadArticles(page, pageSize)
            }
        }.datas
    }

}