package com.vclusters.cloud.main.home.phone.data

import com.android.sdk.net.coroutines.nonnull.executeApiCall
import com.android.sdk.net.coroutines.nullable.executeApiCallNullable
import com.app.base.app.DispatcherProvider
import com.app.base.injection.ApplicationScope
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class CloudPhoneRepository @Inject constructor(
    private val cloudPhoneApi: CloudPhoneApi,
    private val dispatcherProvider: DispatcherProvider,
    @ApplicationScope private val appScope: CoroutineScope
) {

    suspend fun homeAnnouncements(): List<HomeAnnouncement> {
        return withContext(dispatcherProvider.io()) {
            executeApiCall { cloudPhoneApi.loadHomeAnnouncements() }.list
        }
    }

    suspend fun messageCount(): Int {
        return withContext(dispatcherProvider.io()) {
            executeApiCall { cloudPhoneApi.queryMessageCount() }
        }
    }

    suspend fun rebootCloudDevice(deviceId: Int) {
        appScope.async(dispatcherProvider.io()) {
            executeApiCallNullable { cloudPhoneApi.rebootCloudDevice(deviceId.toString()) }
        }.await()
    }

    suspend fun resetCloudDevice(deviceId: Int) {
        appScope.async(dispatcherProvider.io()) {
            executeApiCallNullable { cloudPhoneApi.resetCloudDevice(deviceId.toString()) }
        }.await()
    }

}