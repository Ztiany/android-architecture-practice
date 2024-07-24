package com.app.base.component.usermanager

import android.annotation.SuppressLint
import android.util.Log
import com.android.sdk.cache.getEntity
import com.app.base.data.storage.StorageManager
import com.app.base.debug.ifOpenLog
import com.app.base.utils.json.toJson
import com.app.common.api.usermanager.User
import com.app.common.api.usermanager.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("LogNotTimber")
@Singleton
internal class UserManagerImpl @Inject constructor(storageManager: StorageManager) : UserManager {

    companion object {
        private const val APP_USER_KEY = "app_user_key"
        private const val APP_USER_STORAGE_ID = "app_user_storage_id"
    }

    init {
        // 只在这里用 Log，因为 Timber 可能还没有初始化。
        Log.d("UserManager", "UserManagerImpl initialized.")
    }

    private var currentUser = User.NOT_LOGIN

    private val observableUser: MutableStateFlow<User>

    private val userStorage = storageManager.newStorage(APP_USER_STORAGE_ID)

    init {
        currentUser = userStorage.getEntity(APP_USER_KEY) ?: User.NOT_LOGIN
        observableUser = MutableStateFlow(currentUser)
    }

    override fun saveUser(user: User) {
        currentUser = user
        userStorage.putEntity(APP_USER_KEY, currentUser)
        onUserUpdated()
        ifOpenLog {
            Timber.w("new user: ${currentUser.toJson()}")
        }
    }

    override val user: User
        get() = currentUser

    override fun userFlow() = observableUser

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