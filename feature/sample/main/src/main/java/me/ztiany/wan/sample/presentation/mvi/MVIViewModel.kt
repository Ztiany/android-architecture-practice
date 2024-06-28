package me.ztiany.wan.sample.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.fragment.list.PagingListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.launch
import me.ztiany.wan.sample.data.MVISampleRepository
import me.ztiany.wan.sample.presentation.epoxy.ArticleMapper
import me.ztiany.wan.sample.presentation.epoxy.ArticleVO
import timber.log.Timber
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

    fun send(intent: ArticleIntent) {
        viewModelScope.launch { this@MVIViewModel.intent.emit(intent) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun toPartialChangeFlow(intent: ArticleIntent) = when (intent) {
        is ArticleIntent.Init -> flowOf(
            repository.localArticleFlow,
            repository.firstArticlePage(intent.pageStart, intent.pageSize)
        )
            .flattenMerge()
            .transformWhile {
                emit(articleMapper.mapArticles(it.data))
                Timber.d("Init.toPartialChangeFlow fromRemote=${it.fromRemote}")
                !it.fromRemote // return true to continue, false to stop.
            }
            .map { Init.Success(it) as Init }
            .onStart { emit(Init.Loading) }
            .catch { emit(Init.Fail(it)) }

        is ArticleIntent.More -> flow { emit(articleMapper.mapArticles(repository.loadHomeArticles(intent.pageNo, intent.pageSize))) }
            .map { More.Success(it) as More }
            .onStart { emit(More.Loading) }
            .catch { emit(More.Fail(it)) }

        is ArticleIntent.Report -> repository.reportArticle(intent.id)
            .map { Report.Success(it) as Report }
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