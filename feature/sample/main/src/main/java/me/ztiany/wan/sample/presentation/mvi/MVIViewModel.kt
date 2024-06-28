package me.ztiany.wan.sample.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.fragment.list.PagingListState
import com.android.base.fragment.ui.Paging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.ztiany.wan.sample.data.MVISampleRepository
import me.ztiany.wan.sample.presentation.epoxy.ArticleMapper
import me.ztiany.wan.sample.presentation.epoxy.ArticleVO
import javax.inject.Inject

@HiltViewModel
class MVIViewModel @Inject constructor(
    private val repository: MVISampleRepository,
    private val articleMapper: ArticleMapper,
) : ViewModel() {

    private val intent = MutableSharedFlow<ArticleIntent>()

    private val eventChannel = Channel<ArticleEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val articleState =
        intent
            .flatMapConcat { toPartialChangeFlow(it) }
            .sendEvent()
            .scan(PagingListState<ArticleVO>(isRefreshing = true)) { oldState, partialChange ->
                partialChange.reduce(oldState)
            }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Eagerly, PagingListState(isRefreshing = true))

    private val paging: Paging
        get() = articleState.value.paging

    init {
        send(ArticleIntent.Init)
    }

    fun send(intent: ArticleIntent) {
        viewModelScope.launch { this@MVIViewModel.intent.emit(intent) }
    }

    private fun toPartialChangeFlow(intent: ArticleIntent) = when (intent) {
        is ArticleIntent.Init -> repository.firstArticlePage(paging.start, paging.size)
            .map { Init.success(articleMapper.mapArticles(it)) }
            .onStart { emit(Init.Loading) }
            .catch { emit(Init.Fail(it)) }

        is ArticleIntent.More -> flow {
            emit(articleMapper.mapArticles(repository.loadMoreArticles(paging.next, paging.size)))
        }
            .map { More.success(it) }
            .onStart { emit(More.Loading) }
            .catch { emit(More.Fail(it)) }

        is ArticleIntent.Report -> repository.reportArticle(intent.id)
            .map { Report.success(it) }
    }

    private fun Flow<FeedsPartialChange>.sendEvent(): Flow<FeedsPartialChange> =
        onEach { partialChange ->
            val event = when (partialChange) {
                is Report.Fail -> ArticleEvent.Report.Result("举报失败")
                is Report.Success -> ArticleEvent.Report.Result("举报成功")
                else -> return@onEach
            }
            eventChannel.send(event)
        }

}