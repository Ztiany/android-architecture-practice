package com.vclusters.cloud.main.home.assistant.data

import com.android.sdk.net.coroutines.nonnull.executeApiCall
import com.app.base.app.DispatcherProvider
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class AssistantRepository @Inject constructor(
    private val assistantApi: AssistantApi,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun assistantFeatureConfigs(): List<AssistantFeatureConfig> {
        return withContext(dispatcherProvider.io()) {
            executeApiCall { assistantApi.loadAssistantFeatureConfig() }
        }
    }

}