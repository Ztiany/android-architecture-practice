package me.ztiany.architecture.account.data

import com.app.base.component.usermanager.User
import com.app.base.component.usermanager.UserManager
import com.app.base.config.AppSettings
import kotlinx.coroutines.delay
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val accountApi: AccountApi,
    private val userManager: UserManager,
    private val appSettings: AppSettings
) : AccountDataSource {

    override suspend fun login(phone: String, password: String): User {
        /* val loginRequest = LoginRequest(
             phone = phone,
             password = AESUtils.encryptDataToBase64(password, AESUtils.AES, appSettings.aesKey) ?: "",
         )
         val loginResponse = executeApiCall { accountApi.login(loginRequest) }*/

        delay(1000)
        val loginResponse = LoginResponse(1, "Alien", "a_fake_token")
        val user = User(loginResponse.id, loginResponse.username, phone, loginResponse.token)
        userManager.saveUser(user)
        return user
    }

}