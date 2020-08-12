package com.app.base.data.app

import android.content.Context
import com.android.base.concurrent.SchedulerProvider
import com.android.sdk.net.core.service.ServiceFactory
import com.app.base.data.models.User
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


internal class AppRepository constructor(
        private val context: Context,
        serviceFactory: ServiceFactory,
        private val schedulerProvider: SchedulerProvider,
        private val storageManager: StorageManager
) : AppDataSource {

    companion object {
        //cache key,  user associated
        private const val APP_TOKEN_KEY = "parent_app_token"
        private const val APP_USER_KEY = "parent_user"
    }

    private val observableUser = BehaviorProcessor.create<User>()

    private var currentUser = User.NOT_LOGIN

    private val appStorage = storageManager.userStorage()

    private val stableStorage = storageManager.stableStorage()

    init {
        //init notify user
        currentUser = appStorage.getEntity(APP_USER_KEY, User::class.java) ?: User.NOT_LOGIN
    }

    override fun saveUser(user: User) {
    }

    override fun syncUser(): Completable {
        return Completable.complete()
    }

    override fun userLogined(): Boolean {
        return false
    }

    @Synchronized
    override fun user() = currentUser

    override fun observableUser(): Flowable<User> {
        return observableUser
    }

    @Synchronized
    override fun logout() {
        Timber.d("start logout")
        currentUser = User.NOT_LOGIN
        observableUser.onNext(currentUser)
        storageManager.clearUserAssociated()
        Timber.d("logout success")
    }

}