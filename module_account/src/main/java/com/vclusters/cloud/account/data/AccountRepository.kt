package com.vclusters.cloud.account.data

import com.android.base.utils.security.AESUtils
import com.android.sdk.net.coroutines.nonnull.executeApiCall
import com.app.base.app.AndroidPlatform
import com.app.base.config.AppSettings
import com.app.base.services.usermanager.User
import com.app.base.services.usermanager.UserManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val accountApi: AccountApi,
    private val androidPlatform: AndroidPlatform,
    private val userManager: UserManager,
    private val appSettings: AppSettings,
    private val historyUserStorage: HistoryUserStorage
) : AccountDataSource {

    private val historyUsersFlow = MutableSharedFlow<List<HistoryUser>>(1)

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
            emit(executeApiCall { accountApi.login(loginRequest) })
        }.map {
            val user = User(it.id, it.username, phone, it.token)
            userManager.saveUser(user)
            user
        }
    }

    override fun historyUserList(): Flow<List<HistoryUser>> {
        return historyUsersFlow.also {
            it.tryEmit(historyUserStorage.historyUsers())
        }
    }

    override fun saveHistoryUser(historyUser: HistoryUser) {
        historyUsersFlow.also {
            it.tryEmit(historyUserStorage.addHistoryUser(historyUser))
        }
    }

    override fun deleteHistoryUser(historyUser: HistoryUser) {
        historyUsersFlow.also {
            it.tryEmit(historyUserStorage.deleteHistoryUser(historyUser))
        }
    }

}