package me.ztiany.wan.sample.mvi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.fragment.list.AutoPagingListState
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
import me.ztiany.wan.sample.epoxy.ArticleMapper
import me.ztiany.wan.sample.epoxy.ArticleVO
import me.ztiany.wan.sample.mvi.data.MVISampleRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MVIViewModel @Inject constructor(
    private val repository: MVISampleRepository,
    private val articleMapper: ArticleMapper,
) : ViewModel() {

    private val intent = MutableSharedFlow<ArticleIntent>(1)

    private val eventChannel = Channel<ArticleEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val articleState = intent
        .flatMapConcat { toPartialChangeFlow(it) }
        .sendEvent()
        .scan(AutoPagingListState<ArticleVO>(isRefreshing = true)) { oldState, partialChange ->
            partialChange.reduce(oldState)
        }
        .onEach {
            Timber.d("articleState: ${it.copy(data = it.data.take(1))}")
        }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, AutoPagingListState(isRefreshing = true))

    private val paging: Paging<Int>
        get() = articleState.value.paging

    init {
        send(ArticleIntent.Init)
    }

    fun send(intent: ArticleIntent) {
        viewModelScope.launch {
            this@MVIViewModel.intent.emit(intent)
        }
    }

    private fun toPartialChangeFlow(intent: ArticleIntent): Flow<FeedsPartialChange> {
        Timber.d("toPartialChangeFlow: $intent")
        return when (intent) {
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