package me.ztiany.wan.sample.paging3.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.android.sdk.net.ServiceContext
import com.app.base.utils.IntKeyPagingSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import me.ztiany.wan.sample.common.data.WanAndroidApi
import javax.inject.Inject


@ActivityRetainedScoped
class Paging3SampleRepository @Inject constructor(private val homeApiContext: ServiceContext<WanAndroidApi>) {

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

}