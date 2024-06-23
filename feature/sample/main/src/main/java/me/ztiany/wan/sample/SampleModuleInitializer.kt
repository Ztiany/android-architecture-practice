package me.ztiany.wan.sample

import android.app.Application
import android.content.res.Configuration
import com.android.base.core.AppLifecycle
import com.android.sdk.net.NetContext
import com.android.sdk.net.extension.addHostConfig
import com.app.base.injection.ApplicationScope
import com.app.common.api.dispatcher.DispatcherProvider
import com.app.common.api.errorhandler.ErrorHandler
import com.app.common.api.network.ApiServiceFactoryProvider
import com.app.common.api.usermanager.UserManager
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SampleModuleInitializer @Inject constructor(
    private val serviceFactoryProvider: ApiServiceFactoryProvider,
    private val userManager: UserManager,
    private val errorHandler: ErrorHandler,
    private val dispatcherProvider: DispatcherProvider,
    @ApplicationScope private val scope: CoroutineScope,
) : AppLifecycle {

    override fun onCreate(application: Application) {
        Timber.d("onCreate() called with: application = $application")
        // 添加 Sample 的网络配置
        NetContext.get().addHostConfig(SAMPLE_HOST_FLAG) {
            httpConfig(newHttpConfig())
            errorBodyHandler(newErrorBodyParser(errorHandler))
            aipHandler(newApiHandler(errorHandler))
            exceptionFactory { _, _ -> null }
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