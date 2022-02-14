package com.vclusters.cloud.main.home.assistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.base.utils.common.ignoreCrash
import com.vclusters.cloud.main.home.assistant.data.AssistantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val assistantRepository: AssistantRepository,
    private val featureMapper: FeatureMapper
) : ViewModel() {

    private val _assistantFeatureConfigs = MutableSharedFlow<List<AssistantFeature>>(1)
    val assistantFeatureConfigs: Flow<List<AssistantFeature>> = _assistantFeatureConfigs.asSharedFlow()

    init {
        loadAssistantFeatureConfigs()
    }

    fun loadAssistantFeatureConfigs() {
        viewModelScope.launch {
            ignoreCrash {
                _assistantFeatureConfigs.emit(
                    featureMapper.mapperConfigs(assistantRepository.assistantFeatureConfigs())
                )
            }
        }
    }

}
