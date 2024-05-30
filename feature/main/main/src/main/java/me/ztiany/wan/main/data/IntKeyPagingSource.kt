package me.ztiany.wan.main.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.sdk.net.ServiceContext
import timber.log.Timber

/**
 * PagingSource 通用封装类。
 */
class IntKeyPagingSource<S, SC : ServiceContext<S>, V : Any>(
    private val pageStart: Int,
    private val serviceContext: SC,
    private val load: suspend (SC, Int, Int) -> List<V>,
) : PagingSource<Int, V>() {

    override fun getRefreshKey(state: PagingState<Int, V>): Int? {
        Timber.d("getRefreshKey: anchorPosition=${state.anchorPosition}")

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }?.also {
            Timber.d("getRefreshKey: $it")
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> {
        when (params) {
            is LoadParams.Refresh -> Timber.d("load: Refresh, key=${params.key}, loadSize=${params.loadSize}")
            is LoadParams.Prepend -> Timber.d("load: Prepend, key=${params.key}, loadSize=${params.loadSize}")
            is LoadParams.Append -> Timber.d("load: Append, key=${params.key}, loadSize=${params.loadSize}  ")
        }

        val page = params.key ?: pageStart

        return try {
            val data = load(serviceContext, page, params.loadSize)
            LoadResult.Page(
                data = data,
                prevKey = if (page == pageStart) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}