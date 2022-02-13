package com.vclusters.cloud.main.home.phone.data

import com.android.sdk.net.coroutines.nonnull.executeApiCall
import com.app.base.injection.ApplicationScope
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

@ActivityRetainedScoped
class CloudPhoneRepository @Inject constructor(
    private val cloudPhoneApi: CloudPhoneApi,
    @ApplicationScope private val appScope: CoroutineScope
) {

    suspend fun homeAnnouncements(): List<HomeAnnouncement> {
        return executeApiCall { cloudPhoneApi.loadHomeAnnouncements() }.list
    }

    suspend fun messageCount(): Int {
        return executeApiCall { cloudPhoneApi.queryMessageCount() }
    }

    suspend fun rebootCloudDevice(userCardId: Int) {
        appScope.async {
            executeApiCall { cloudPhoneApi.rebootCloudDevice(userCardId.toString()) }
        }.await()
    }

    suspend fun resetCloudDevice(userCardId: Int) {
        appScope.async {
            executeApiCall { cloudPhoneApi.resetCloudDevice(userCardId.toString()) }
        }.await()
    }

}