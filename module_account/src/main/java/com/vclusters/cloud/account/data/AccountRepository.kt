package com.vclusters.cloud.account.data

import com.android.base.utils.security.AESUtils
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.coroutines.apiCall
import com.android.sdk.net.coroutines.onSucceeded
import com.app.base.app.AndroidPlatform
import com.app.base.config.AppSettings
import com.app.base.services.usermanager.UserManager
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class AccountRepository @Inject constructor(
    private val accountApi: AccountApi,
    private val androidPlatform: AndroidPlatform,
    private val userManager: UserManager,
    private val appSettings: AppSettings
) : AccountDataSource {

    override suspend fun login(phone: String, password: String): CallResult<LoginResponse> {
        val loginRequest = LoginRequest(
            phone = "18888888888",
            password = AESUtils.encryptDataToBase64("123456789", AESUtils.AES, appSettings.aesKey) ?: "",
            imei = androidPlatform.getDeviceId(),
            diskName = androidPlatform.getDeviceName(),
            uuid = androidPlatform.getDeviceId(),
            mac = androidPlatform.getMAC(),
            ipAddr = androidPlatform.getIpAddress(),
        )
        return apiCall { accountApi.login(loginRequest) }.onSucceeded {

        }
    }

}