package me.ztiany.wan.sample.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.fragment.list.SimpleListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.launch
import me.ztiany.wan.sample.data.SampleRepository
import me.ztiany.wan.sample.presentation.epoxy.ArticleVO
import me.ztiany.wan.sample.presentation.epoxy.ArticleVOMapper
import timber.log.Timber
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

@HiltViewModel
class MVIViewModel @Inject constructor(
    private val repository: SampleRepository,
    private val articleVOMapper: ArticleVOMapper,
) : ViewModel() {

    private val intent = MutableSharedFlow<ArticleIntent>()

    private val eventChannel = Channel<ArticleEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    val articleState =
        intent
            .onEach {
                Timber.d("newsState feedsIntent intent=$it")
            }
            .toPartialChangeFlow()
            .onEach { it: FeedsPartialChange ->
                Timber.d("newsState feedsIntent partialChange=$it")
            }
            .sendEvent()
            .scan(SimpleListState<ArticleVO>(isLoadingMore = true)) { oldState, partialChange ->
                partialChange.reduce(oldState)
            }
            .onEach {it: SimpleListState<ArticleVO> ->
                Timber.d("newsState onEach() newState=$it")
            }
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Eagerly, SimpleListState(isLoadingMore = true))

    fun send(intent: ArticleIntent) {
        viewModelScope.launch { this@MVIViewModel.intent.emit(intent) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<ArticleIntent>.toPartialChangeFlow(): Flow<FeedsPartialChange> = merge(
        filterIsInstance<ArticleIntent.Init>().flatMapConcat { it.toPartialChangeFlow() },
        filterIsInstance<ArticleIntent.More>().flatMapConcat { it.toPartialChangeFlow() },
        filterIsInstance<ArticleIntent.Report>().flatMapConcat { it.toPartialChangeFlow() },
    )

    private fun ArticleIntent.More.toPartialChangeFlow() =
        flow {
            emit(articleVOMapper.mapArticles(repository.loadArticles(2, count)))
        }
            .map { if (it.isEmpty()) More.Fail("no more news") else More.Success(it) }
            .onStart { emit(More.Loading) }
            .catch { emit(More.Fail("load more failed by xxx")) }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun ArticleIntent.Init.toPartialChangeFlow() =
        flowOf(
            repository.localNewsOneShotFlow,
            repository.remoteNewsFlow(1, this.count)
        )
            .flattenMerge()
            .transformWhile {
                emit(articleVOMapper.mapArticles(it.news))
                !it.abort
            }
            .map { news -> if (news.isEmpty()) Init.Fail("no more news") else Init.Success(news) }
            .onStart { emit(Init.Loading) }
            .catch {
                if (it is SSLHandshakeException)
                    emit(Init.Fail("network error,show old news"))
            }


    private fun ArticleIntent.Report.toPartialChangeFlow() =
        repository.reportNews(id)
            .map { if (it >= 0L) Report.Success(it) else Report.Fail }
            .catch { emit((Report.Fail)) }

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