package me.ztiany.wan.sample.presentation.mvi

import com.android.base.fragment.list.PagingListState
import me.ztiany.wan.sample.presentation.epoxy.ArticleVO
import me.ztiany.wan.sample.presentation.mvi.Init.Success

sealed interface FeedsPartialChange {
    fun reduce(oldState: PagingListState<ArticleVO>): PagingListState<ArticleVO>
}

sealed class Init : FeedsPartialChange {

    override fun reduce(oldState: PagingListState<ArticleVO>): PagingListState<ArticleVO> = when (this) {
        Loading -> oldState.toRefreshing()
        is Success -> oldState.replaceList(list, list.isNotEmpty())
        is Fail -> oldState.toRefreshError(error)
    }

    data object Loading : Init()

    data class Success(val list: List<ArticleVO>) : Init()

    data class Fail(val error: Throwable) : Init()

    companion object {
        fun success(list: List<ArticleVO>): Init = Success(list)
    }
}

sealed class More : FeedsPartialChange {

    override fun reduce(oldState: PagingListState<ArticleVO>): PagingListState<ArticleVO> = when (this) {
        Loading -> oldState.toLoadingMore()
        is Success -> oldState.appendList(list, list.isNotEmpty())
        is Fail -> oldState.toLoadMoreError(error)
    }

    data object Loading : More()

    data class Success(val list: List<ArticleVO>) : More()

    data class Fail(val error: Throwable) : More()

    companion object {
        fun success(list: List<ArticleVO>): More = Success(list)
    }
}

sealed class Report : FeedsPartialChange {

    override fun reduce(oldState: PagingListState<ArticleVO>): PagingListState<ArticleVO> = when (this) {
        is Success -> oldState.copy(data = oldState.data.filterNot { it.id == id })
        is Fail -> oldState
    }

    class Success(val id: Int) : Report()

    data class Fail(val error: Throwable) : Report()

    companion object {
        fun success(id: Int): Report = Success(id)
    }

}