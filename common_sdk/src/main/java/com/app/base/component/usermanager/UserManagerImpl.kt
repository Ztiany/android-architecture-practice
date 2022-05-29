package com.app.base.component.usermanager

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
        private const val APP_USER_KEY = "APP_USER_KEY"
    }

    private var currentUser = User.NOT_LOGIN

    private val observableUser: MutableStateFlow<User>

    private val userStorage = storageManager.userStorage()

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
        storageManager.clearUserAssociated()
        onUserUpdated()
        Timber.d("logout success")
    }

    private fun onUserUpdated() {
        observableUser.value = currentUser
    }

}