package com.app.base.services.usermanager

import com.android.sdk.cache.getEntity
import com.android.sdk.net.coroutines.executeApiCall
import com.android.sdk.net.extension.create
import com.app.base.app.ServiceProvider
import com.app.base.data.storage.StorageManager
import com.app.base.debug.ifOpenDebug
import com.app.base.utils.JsonUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserManagerImpl @Inject constructor(
    serviceProvider: ServiceProvider,
    private val storageManager: StorageManager
) : UserManager {

    companion object {
        private const val APP_USER_TOKEN_KEY = "APP_USER_TOKEN_KEY"
        private const val APP_USER_DATA_KEY = "APP_USER_KEY"
    }

    private val observableUser = MutableStateFlow(User.NOT_LOGIN)

    private val userApi by lazy {
        serviceProvider.getDefault().create<UserApi>()
    }

    private var currentUser = User.NOT_LOGIN

    private val userStorage = storageManager.userStorage()

    init {
        currentUser = userStorage.getEntity(APP_USER_DATA_KEY) ?: User.NOT_LOGIN
        ifOpenDebug {
            Timber.d("current user: %s", JsonUtils.toJson(currentUser))
        }
        onUserUpdated()
    }

    private fun saveUser(user: User) {
        currentUser = user
        userStorage.putEntity(APP_USER_DATA_KEY, currentUser)
        onUserUpdated()
        ifOpenDebug {
            Timber.w("new user: ${JsonUtils.toJson(currentUser)}")
        }
    }

    @Synchronized
    override fun user(): User {
        return currentUser
    }

    override fun saveUserToken(token: String) {
        userStorage.putString(APP_USER_TOKEN_KEY, token)
    }

    override fun getUserToken(): String {
        return userStorage.getString(APP_USER_TOKEN_KEY, "")
    }

    override fun syncUserInfo() = flow<User> {
        emit(executeApiCall(requireNonNullData = true) { userApi.loadUserInfo() })
    }.onEach {
        saveUser(it)
    }

    override fun isUserLogin() = user().isLogin()

    override fun subscribeUser() = observableUser

    override fun logout() {
        Timber.d("start logout")
        currentUser = User.NOT_LOGIN
        storageManager.clearUserAssociated()
        onUserUpdated()
        Timber.d("logout success")
    }

    private fun onUserUpdated() {
        observableUser.value = currentUser
    }

}