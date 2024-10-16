package com.app.sample.compose.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sdk.net.coroutines.onError
import com.android.sdk.net.coroutines.onSuccess
import com.android.sdk.net.extension.map
import com.app.base.compose.architect.state.PageData
import com.app.sample.compose.vo.ArticleMapper
import com.app.sample.compose.vo.ArticleVO
import com.app.sample.compose.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RefreshStateViewModel @Inject constructor(
    private val sampleRepository: Repository,
    private val articleMapper: ArticleMapper,
) : ViewModel() {

    private val queryCondition = MutableStateFlow(
        QueryCondition(
            refreshAction = 0,
            pageStart = 0,
            pageSize = 5
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val articles = queryCondition
        .flatMapMerge(1) {
            loadEmployeeList(it)
        }.scan(PageData<List<ArticleVO>>(isLoading = true)) { accumulator, value ->
            // always dispatch the latest data.
            if (value.data == null) {
                value.copy(accumulator.data)
            } else value
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000), PageData(isLoading = true))

    private fun loadEmployeeList(queryCondition: QueryCondition) = flow<PageData<List<ArticleVO>>> {
        emit(PageData(isLoading = true))

        sampleRepository.loadHomeArticlesCallback(
            queryCondition.pageStart,
            queryCondition.pageSize,
        ).map {
            articleMapper.mapArticles(it)
        }.onSuccess {
            emit(PageData(data = it))
        }.onError {
            emit(PageData(refreshError = it))
        }
    }

    fun refresh() {
        queryCondition.update {
            it.copy(refreshAction = it.refreshAction + 1)
        }
    }

}