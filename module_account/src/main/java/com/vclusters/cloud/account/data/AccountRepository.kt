package com.vclusters.cloud.account.data

import com.android.base.utils.security.AESUtils
import com.android.sdk.net.coroutines.executeApiCall
import com.app.base.app.AndroidPlatform
import com.app.base.config.AppSettings
import com.app.base.services.usermanager.User
import com.app.base.services.usermanager.UserManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val accountApi: AccountApi,
    private val androidPlatform: AndroidPlatform,
    private val userManager: UserManager,
    private val appSettings: AppSettings
) : AccountDataSource {

    override fun login(phone: String, password: String): Flow<User> {
        val loginRequest = LoginRequest(
            phone = phone,
            password = AESUtils.encryptDataToBase64(password, AESUtils.AES, appSettings.aesKey) ?: "",
            imei = androidPlatform.getDeviceId(),
            diskName = androidPlatform.getDeviceName(),
            uuid = androidPlatform.getDeviceId(),
            mac = androidPlatform.getMAC(),
            ipAddr = androidPlatform.getIpAddress(),
        )
        return flow {
            emit(executeApiCall(requireNonNullData = true) { accountApi.login(loginRequest) })
        }.flatMapConcat {
            Timber.d("-----------------------------")
            userManager.saveUserToken(it.token)
            userManager.syncUserInfo()
        }

    }

}