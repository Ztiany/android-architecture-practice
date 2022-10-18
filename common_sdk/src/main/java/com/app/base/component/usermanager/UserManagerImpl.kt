package com.app.base.component.usermanager

import com.android.common.api.usermanager.User
import com.android.common.api.usermanager.UserManager
import com.android.sdk.cache.getEntity
import com.app.base.data.storage.StorageManager
import com.app.base.debug.ifOpenDebug
import com.app.base.utils.JsonUtils
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserManagerImpl @Inject constructor(
    private val storageManager: StorageManager
) : UserManager {

    companion object {
        private const val APP_USER_KEY = "app_user_key"
        private const val APP_USER_STORAGE_ID = "app_user_storage_id"
    }

    private var currentUser = User.NOT_LOGIN

    private val observableUser: MutableStateFlow<User>

    private val userStorage = storageManager.newStorage(APP_USER_STORAGE_ID)

    init {
        currentUser = userStorage.getEntity(APP_USER_KEY) ?: User.NOT_LOGIN
        ifOpenDebug {
            Timber.d("current user: %s", JsonUtils.toJson(currentUser))
        }
        observableUser = MutableStateFlow(currentUser)
    }

    override fun saveUser(user: User) {
        currentUser = user
        userStorage.putEntity(APP_USER_KEY, currentUser)
        onUserUpdated()
        ifOpenDebug {
            Timber.w("new user: ${JsonUtils.toJson(currentUser)}")
        }
    }

    override val user: User
        get() = currentUser

    override fun subscribeUser() = observableUser

    override fun logout() {
        Timber.d("start logout")
        currentUser = User.NOT_LOGIN
        userStorage.clearAll()
        onUserUpdated()
        Timber.d("logout success")
    }

    private fun onUserUpdated() {
        observableUser.value = currentUser
    }

}