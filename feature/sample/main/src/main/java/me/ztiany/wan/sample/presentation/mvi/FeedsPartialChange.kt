package me.ztiany.wan.sample.presentation.mvi

import com.android.base.fragment.list.SimpleListState
import me.ztiany.wan.sample.presentation.epoxy.ArticleVO

sealed interface FeedsPartialChange {
    fun reduce(oldState: SimpleListState<ArticleVO>): SimpleListState<ArticleVO>
}

sealed class Init : FeedsPartialChange {

    override fun reduce(oldState: SimpleListState<ArticleVO>): SimpleListState<ArticleVO> = when (this) {
        Loading -> oldState.toRefreshing()
        is Success -> oldState.replaceList(list, true)
        is Fail -> oldState.toRefreshError(Throwable(error))
    }

    data object Loading : Init()
    data class Success(val list: List<ArticleVO>) : Init()
    data class Fail(val error: String) : Init()
}

sealed class More : FeedsPartialChange {

    override fun reduce(oldState: SimpleListState<ArticleVO>): SimpleListState<ArticleVO> = when (this) {
        Loading -> oldState.toLoadingMore()
        is Success -> oldState.appendList(list, true)
        is Fail -> oldState.toLoadMoreError(Throwable(error))
    }

    data object Loading : More()
    data class Success(val list: List<ArticleVO>) : More()
    data class Fail(val error: String) : More()
}

sealed class Report : FeedsPartialChange {

    override fun reduce(oldState: SimpleListState<ArticleVO>): SimpleListState<ArticleVO> = when (this) {
        is Success -> oldState.copy(data = oldState.data.filterNot { it.id == id })
        Fail -> oldState
    }

    class Success(val id: Int) : Report()

    data object Fail : Report()

}