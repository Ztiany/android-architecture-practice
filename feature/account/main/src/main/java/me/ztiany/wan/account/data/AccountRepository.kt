package me.ztiany.wan.account.data

import com.android.sdk.net.coroutines.executeApiCall
import com.app.base.app.Platform
import com.app.common.api.dispatcher.DispatcherProvider
import com.app.common.api.usermanager.User
import com.app.common.api.usermanager.UserManager
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AccountRepository @Inject constructor(
    private val accountApi: AccountApi,
    private val userManager: UserManager,
    private val platform: Platform,
    private val dispatcherProvider: DispatcherProvider
) : AccountDataSource {

    override suspend fun smsLogin(phone: String, smsCode: String): User {
        val loginRequest = LoginRequest(
            phoneNumber = phone,
            code = smsCode,
            type = CodeRequest.TYPE_LOGIN,
            devicesId = platform.getDeviceId(),
            diskName = platform.getDeviceName(),
            versionNumber = platform.getAppVersionNumber()
        )
        val loginResponse = withContext(dispatcherProvider.io()) {
            executeApiCall { accountApi.codeLogin(loginRequest) }
        }
        return User(
            id = loginResponse.id,
            uid = loginResponse.uid,
            isCertified = loginResponse.isCertified,
            headImgUrl = loginResponse.headImgUrl,
            phoneNumber = loginResponse.phoneNumber,
            userName = loginResponse.userName,
            token = loginResponse.token
        ).apply {
            userManager.saveUser(this)
        }
    }

    override suspend fun sendLoginCode(phone: String) {
        return withContext(dispatcherProvider.io()) {
            executeApiCall {
                accountApi.sendLoginCode(
                    CodeRequest(
                        phone = phone,
                        type = CodeRequest.TYPE_LOGIN
                    )
                )
            }
        }
    }

}