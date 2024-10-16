package com.app.sample.compose.ui.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.foundation.state.State
import com.app.base.utils.statex.transformToStateFlow
import com.app.sample.compose.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(repository: Repository) : ViewModel() {

    private val uiIntent = MutableSharedFlow<RequestIntent>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val remarkState = uiIntent.filterIsInstance<Query>()
        .flatMapMerge {
            suspend {
                repository.fakeQuery()
            }.transformToStateFlow()
        }.stateIn(viewModelScope, SharingStarted.Eagerly, State.loading())

    @OptIn(ExperimentalCoroutinesApi::class)
    val updateRemarkState = uiIntent.filterIsInstance<Update>()
        .flatMapMerge {
            suspend {
                repository.fakeUpdate(it.open)
            }.transformToStateFlow()
        }.shareIn(viewModelScope, SharingStarted.Lazily)

    fun sendIntent(intent: RequestIntent) {
        viewModelScope.launch { uiIntent.emit(intent) }
    }

}