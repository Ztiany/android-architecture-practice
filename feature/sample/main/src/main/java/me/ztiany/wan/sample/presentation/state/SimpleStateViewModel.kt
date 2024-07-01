package me.ztiany.wan.sample.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.fragment.state.SimpleDataState
import com.android.sdk.net.coroutines.onError
import com.android.sdk.net.coroutines.onSuccess
import com.android.sdk.net.extension.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import me.ztiany.wan.sample.data.SampleRepository
import me.ztiany.wan.sample.presentation.epoxy.ArticleMapper
import me.ztiany.wan.sample.presentation.epoxy.ArticleVO
import javax.inject.Inject

@HiltViewModel
class SimpleStateViewModel @Inject constructor(
    private val sampleRepository: SampleRepository,
    private val articleMapper: ArticleMapper,
) : ViewModel() {

    private val queryCondition = MutableStateFlow(
        QueryCondition(
            refreshAction = 0,
            pageStart = 0,
            pageSize = 30
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val articles = queryCondition
        .flatMapMerge(1) {
            loadEmployeeList(it)
        }.scan(SimpleDataState<List<ArticleVO>>(isRefreshing = true)) { accumulator, value ->
            // always dispatch the latest data.
            if (value.data == null) {
                value.copy(accumulator.data)
            } else value
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000), SimpleDataState(isRefreshing = true))

    private fun loadEmployeeList(queryCondition: QueryCondition) = flow<SimpleDataState<List<ArticleVO>>> {
        emit(SimpleDataState(isRefreshing = true))

        sampleRepository.loadHomeArticlesCallback(
            queryCondition.pageStart,
            queryCondition.pageSize,
        ).map {
            articleMapper.mapArticles(it)
        }.onSuccess {
            emit(SimpleDataState(data = it))
        }.onError {
            emit(SimpleDataState(refreshError = it))
        }
    }

    fun refresh() {
        queryCondition.update {
            it.copy(refreshAction = it.refreshAction + 1)
        }
    }

}