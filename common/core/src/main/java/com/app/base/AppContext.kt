package com.app.base

import android.accounts.NetworkErrorException
import android.app.Application
import android.content.res.Configuration
import com.android.base.core.AndroidSword
import com.android.base.core.AppLifecycle
import com.android.base.core.BaseAppContext
import com.android.base.core.RequestErrorClassifier
import com.android.base.core.RequestErrorHandler
import com.android.base.fragment.anim.HorizontalTransitions
import com.android.base.fragment.fragmentModule
import com.android.base.utils.BaseUtils
import com.android.sdk.net.core.exception.ServerErrorException
import com.android.sdk.upgrade.AppUpgradeChecker
import com.app.apm.APM
import com.app.base.app.ComponentProcessor
import com.app.base.app.Platform
import com.app.base.config.AppSettings
import com.app.base.data.protocol.ApiProtocol
import com.app.base.dialog.loading.AppLoadingViewHost
import com.app.base.upgrade.AppUpgradeInteractor
import com.app.common.api.errorhandler.ErrorHandler
import com.app.common.api.router.AppRouter
import com.app.common.api.usermanager.UserManager
import dagger.Lazy
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * @author Ztiany
 */
abstract class AppContext : BaseAppContext() {

    @Inject internal lateinit var userManager: Lazy<UserManager>

    @Inject internal lateinit var errorHandler: Lazy<ErrorHandler>

    @Inject internal lateinit var appRouter: Lazy<AppRouter>

    @Inject internal lateinit var appSettings: Lazy<AppSettings>

    @Inject internal lateinit var appUpgradeInteractor: Lazy<AppUpgradeInteractor>

    @Inject internal lateinit var platform: Lazy<Platform>

    @Inject internal lateinit var apiProtocol: Lazy<ApiProtocol>

    @Inject internal lateinit var moduleInitializers: Set<@JvmSuppressWildcards AppLifecycle>

    override fun onCreate() {
        application = this
        super.onCreate()
        configFoundation()

        moduleInitializers.forEach {
            it.onCreate(this)
        }
    }

    private fun configFoundation() {
        BaseUtils.init(this)
        APM.init(this).start()
        appSettings.get().init()
        apiProtocol.get().initHttpConfig()

        registerActivityLifecycleCallbacks(ComponentProcessor())

        // lib base config
        with(AndroidSword) {
            // 错误消息转换器
            requestErrorHandler = object : RequestErrorHandler {
                override fun handle(throwable: Throwable) {
                    errorHandler.get().handleError(throwable)
                }
            }
            //错误分类器
            requestErrorClassifier = object : RequestErrorClassifier {
                override fun isNetworkError(throwable: Throwable) = throwable is NetworkErrorException || throwable is IOException
                override fun isServerError(throwable: Throwable) =
                    throwable is ServerErrorException || throwable is HttpException && throwable.code() >= 500/*http internal error*/
            }

            //Dialog 最短展示时间
            minimalDialogDisplayTime = appSettings.get().minimumDialogShowTime

            fragmentModule {
                //分页开始页码
                defaultPageStart = appSettings.get().defaultPageStart
                //默认分页大小
                defaultPageSize = appSettings.get().defaultPageSize
                //默认的 Fragment 容器 id
                defaultFragmentContainerId = R.id.common_container_id
                //默认的 Fragment 转场动画
                defaultFragmentTransitions = HorizontalTransitions()
                //默认的通用的 LoadingDialog 和 Toast 实现
                loadingViewHostFactory = { AppLoadingViewHost(it) }
            }
        }

        AppUpgradeChecker.installInteractor(appUpgradeInteractor.get())
    }

    override fun onLowMemory() {
        super.onLowMemory()
        moduleInitializers.forEach {
            it.onLowMemory()
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        moduleInitializers.forEach {
            it.onTrimMemory(level)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        moduleInitializers.forEach {
            it.onConfigurationChanged(newConfig)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        moduleInitializers.forEach {
            it.onTerminate()
        }
    }

    companion object {
        private var application by Delegates.notNull<Application>()

        fun get(): Application {
            return application
        }
    }

}