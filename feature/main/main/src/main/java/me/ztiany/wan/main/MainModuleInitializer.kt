package me.ztiany.wan.main

import android.app.Application
import android.content.res.Configuration
import com.android.base.core.AppLifecycle
import com.app.base.app.DispatcherProvider
import com.app.base.injection.ApplicationScope
import com.app.common.api.network.ApiServiceFactoryProvider
import com.app.common.api.usermanager.UserManager
import com.blankj.utilcode.util.ProcessUtils
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MainModuleInitializer @Inject constructor(
    private val serviceFactoryProvider: ApiServiceFactoryProvider,
    private val userManager: UserManager,
    @ApplicationScope private val scope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) : AppLifecycle {

    override fun onCreate(application: Application) {
        Timber.d("onCreate() called with: application = $application")
        if (ProcessUtils.isMainProcess()) {
            TokenVerifier(userManager, serviceFactoryProvider, scope, dispatcherProvider).start()
        }
    }

    override fun onTerminate() {
    }

    override fun onTrimMemory(level: Int) {
    }

    override fun onLowMemory() {
    }

    override fun onConfigurationChanged(newConfig: Configuration) {

    }

}