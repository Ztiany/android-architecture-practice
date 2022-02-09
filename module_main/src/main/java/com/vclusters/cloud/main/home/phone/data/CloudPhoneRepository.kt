package com.vclusters.cloud.main.home.phone.data

import com.android.sdk.net.coroutines.nonnull.executeApiCall
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class CloudPhoneRepository @Inject constructor(
    private val cloudPhoneApi: CloudPhoneApi
) {

    suspend fun homeAnnouncements(): List<HomeAnnouncement> {
        return executeApiCall { cloudPhoneApi.loadHomeAnnouncements() }.list
    }

    suspend fun messageCount(): Int {
        return executeApiCall { cloudPhoneApi.queryMessageCount() }
    }

}