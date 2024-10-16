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
import me.ztiany.wan.sample.common.net.SAMPLE_HOST_FLAG
import me.ztiany.wan.sample.common.net.newErrorBodyParser
import me.ztiany.wan.sample.common.net.newErrorListener
import me.ztiany.wan.sample.common.net.newHttpConfig
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SampleModuleInitializer @Inject constructor(
    private val userManager: UserManager,
    private val errorHandler: ErrorHandler,
    private val dispatcherProvider: DispatcherProvider,
    private val serviceFactoryProvider: ApiServiceFactoryProvider,
    @ApplicationScope private val scope: CoroutineScope,
) : AppLifecycle {

    override fun onCreate(application: Application) {
        Timber.d("onCreate() called with: application = $application")
        // 添加 Sample 的网络配置
        NetContext.get().addHostConfig(SAMPLE_HOST_FLAG) {
            httpConfig(newHttpConfig())
            errorBodyParser(newErrorBodyParser(errorHandler))
            errorListener(newErrorListener(errorHandler))
            apiErrorFactory { _, _ -> null }
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