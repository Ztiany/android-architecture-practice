package com.app.base.services.usermanager

import android.content.Context
import com.android.sdk.cache.getEntity
import com.app.base.data.storage.StorageManager
import com.app.base.utils.JsonUtils
import com.app.base.debug.ifOpenDebug
import io.reactivex.Completable
import io.reactivex.processors.BehaviorProcessor
import timber.log.Timber

internal class AppRepository(
    private val context: Context,
    private val storageManager: StorageManager
) : AppDataSource {

    companion object {
        //cache key,  user associated
        private const val APP_USER_KEY = "sys_user_login_key"
    }

    private val observableUser = BehaviorProcessor.create<User>()

    private var currentUser = User.NOT_LOGIN

    private val userStorage = storageManager.userStorage()

    init {
        currentUser = userStorage.getEntity(APP_USER_KEY) ?: User.NOT_LOGIN

        ifOpenDebug {
            Timber.d("init AppDataSource start------------------------------------------------->")
            Timber.d("user: %s", JsonUtils.toJson(currentUser))
            Timber.d("init AppDataSource end<-------------------------------------------------")
        }

        observableUser.onNext(currentUser)
    }

    override fun saveUser(user: User) {
        saveUserSynchronized(user)
    }

    @Synchronized
    private fun saveUserSynchronized(user: User) {
        if (currentUser == user) {
            ifOpenDebug {
                Timber.w("save same user, ignore it-------------------------------------------------")
                Timber.w(JsonUtils.toJson(user))
            }
            return
        }

        currentUser = user
        userStorage.putEntity(APP_USER_KEY, currentUser)
        observableUser.onNext(currentUser)

        ifOpenDebug {
            Timber.w("save new user -------------------------------------------------")
            Timber.w(JsonUtils.toJson(currentUser))
        }
    }

    @Synchronized
    override fun user(): User {
        return currentUser
    }

    override fun userLogined() = user().logined()

    override fun observableUser() = observableUser

    override fun logout(askRemote: Boolean): Completable {
        clearUserData()
        return Completable.complete()
    }

    @Synchronized
    private fun clearUserData() {
        Timber.d("start logout")
        currentUser = User.NOT_LOGIN
        observableUser.onNext(currentUser)
        storageManager.clearUserAssociated()
        Timber.d("logout success")
    }

}