package com.app.base

import android.accounts.NetworkErrorException
import android.app.Application
import android.content.res.Configuration
import com.android.base.core.AndroidSword
import com.android.base.core.ErrorClassifier
import com.android.base.core.ErrorConvert
import com.android.base.core.AppLifecycle
import com.android.base.core.BaseAppContext
import com.android.base.fragment.defaultFragmentAnimator
import com.android.base.fragment.defaultFragmentContainerId
import com.android.base.fragment.defaultPageSize
import com.android.base.fragment.defaultPageStart
import com.android.base.fragment.loadingViewHostFactory
import com.android.base.utils.BaseUtils
import com.android.sdk.mediaselector.common.MediaSelectorConfiguration
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ServerErrorException
import com.android.sdk.net.extension.init
import com.android.sdk.net.extension.setDefaultHostConfig
import com.android.sdk.upgrade.AppUpgradeChecker
import com.android.sdk.upgrade.impl.AppUpgradeInteractor
import com.app.base.app.AndroidPlatform
import com.app.base.app.ComponentProcessor
import com.app.base.app.ErrorHandler
import com.app.base.app.FragmentScaleAnimator
import com.app.base.config.AppSettings
import com.app.base.data.protocol.newApiHandler
import com.app.base.data.protocol.newErrorBodyParser
import com.app.base.data.protocol.newErrorMessage
import com.app.base.data.protocol.newHttpConfig
import com.app.base.data.protocol.newPlatformInteractor
import com.app.base.debug.DebugTools
import com.app.base.widget.dialog.AppLoadingViewHost
import com.app.common.api.router.AppRouter
import com.app.common.api.usermanager.UserManager
import com.app.apm.APM
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

    @Inject internal lateinit var androidPlatform: Lazy<AndroidPlatform>

    @Inject internal lateinit var moduleInitializers: Set<@JvmSuppressWildcards AppLifecycle>

    override fun onCreate() {
        application = this
        super.onCreate()
        BaseUtils.init(this)
        DebugTools.init(this)
        APM.init(this).start()
        appSettings.get().init()
        configNetworkApi()
        configFoundation()
        configLibraries()
        configThirdSDK()
        moduleInitializers.forEach {
            it.onCreate(this)
        }
    }

    private fun configNetworkApi() {
        NetContext.get().init(this) {
            errorMessage(newErrorMessage())
            platformInteractor(newPlatformInteractor(androidPlatform.get()))
        }.setDefaultHostConfig {
            httpConfig(newHttpConfig(userManager.get(), appSettings.get(), androidPlatform.get(), errorHandler.get()))
            errorBodyHandler(newErrorBodyParser(errorHandler.get()))
            aipHandler(newApiHandler(errorHandler.get()))
            exceptionFactory { _, _ -> null }
        }
    }

    private fun configFoundation() {
        //安装 Activity/Fragment 注入器
        registerActivityLifecycleCallbacks(ComponentProcessor())
        //lib-base 配置
        with(AndroidSword) {
            //默认的 Fragment 容器 id
            defaultFragmentContainerId = R.id.common_container_id
            //默认的 Fragment 转场动画
            defaultFragmentAnimator = FragmentScaleAnimator()
            //Dialog 最短展示时间
            minimalDialogDisplayTime = appSettings.get().minimumDialogShowTime
            //默认的通用的 LoadingDialog 和 Toast 实现
            loadingViewHostFactory = { AppLoadingViewHost(it) }
            //分页开始页码
            defaultPageStart = appSettings.get().defaultPageStart
            //默认分页大小
            defaultPageSize = appSettings.get().defaultPageSize
            //错误消息转换器
            errorConvert = object : ErrorConvert {
                override fun convert(throwable: Throwable): CharSequence {
                    return errorHandler.get().generateMessage(throwable)
                }
            }
            //错误分类器
            errorClassifier = object : ErrorClassifier {
                override fun isNetworkError(throwable: Throwable) = throwable is NetworkErrorException || throwable is IOException
                override fun isServerError(throwable: Throwable) =
                    throwable is ServerErrorException || throwable is HttpException && throwable.code() >= 500/*http internal error*/
            }
        }
    }

    private fun configLibraries() {
        MediaSelectorConfiguration.forceUseLegacyApi(true)
        AppUpgradeChecker.installInteractor(appUpgradeInteractor.get())
    }

    private fun configThirdSDK() {
        //WeChatManager.initWeChatSDK(this, "", "")
        //Umeng.init(this)
        //Bugly.init(this)
        //PushManager.getInstance().init(this)
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