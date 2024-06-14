package me.ztiany.wan.account.data

import com.android.sdk.net.coroutines.executeApiCall
import com.app.base.app.AndroidPlatform
import com.app.common.api.dispatcher.DispatcherProvider
import com.app.common.api.usermanager.User
import com.app.common.api.usermanager.UserManager
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val accountApi: AccountApi,
    private val userManager: UserManager,
    private val androidPlatform: AndroidPlatform,
    private val dispatcherProvider: DispatcherProvider
) : AccountDataSource {

    override suspend fun smsLogin(phone: String, smsCode: String): User {
        val loginRequest = LoginRequest(
            phoneNumber = phone,
            code = smsCode,
            type = CodeRequest.TYPE_LOGIN,
            devicesId = androidPlatform.getDeviceId(),
            diskName = androidPlatform.getDeviceName(),
            versionNumber = androidPlatform.getAppVersionNumber()
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