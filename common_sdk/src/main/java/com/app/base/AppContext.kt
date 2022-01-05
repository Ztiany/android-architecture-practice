package com.app.base

import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDex
import com.android.base.AndroidSword
import com.android.base.ErrorClassifier
import com.android.base.architecture.app.AppLifecycle
import com.android.base.architecture.app.BaseAppContext
import com.android.sdk.mediaselector.common.MediaSelectorConfiguration
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.NetworkErrorException
import com.android.sdk.net.core.exception.ServerErrorException
import com.android.sdk.net.extension.addHostConfig
import com.android.sdk.net.extension.init
import com.android.sdk.upgrade.AppUpgradeChecker
import com.app.base.app.*
import com.app.base.config.AppSettings
import com.app.base.debug.DebugTools
import com.app.base.router.AppRouter
import com.app.base.services.usermanager.UserManager
import com.app.base.upgrade.AppUpgradeInteractor
import com.app.base.widget.dialog.AppLoadingView
import dagger.Lazy
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 10:20
 */
abstract class AppContext : BaseAppContext() {

    @Inject internal lateinit var userManager: Lazy<UserManager>

    @Inject internal lateinit var errorHandler: Lazy<ErrorHandler>

    @Inject internal lateinit var appRouter: Lazy<AppRouter>

    @Inject internal lateinit var appSettings: Lazy<AppSettings>

    @Inject internal lateinit var appUpgradeInteractor: Lazy<AppUpgradeInteractor>

    @Inject internal lateinit var moduleInitializers: Set<@JvmSuppressWildcards AppLifecycle>

    override fun attachBaseContext(base: Context) {
        MultiDex.install(this)
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        DebugTools.init(this)
        appSettings.get().init()
        appRouter.get().initRouter(this, true)
        configNetworkApi()
        configFoundation()
        configLibraries()
        configThirdSDK()
        moduleInitializers.forEach {
            it.onCreate(this)
        }
    }

    private fun configNetworkApi() {
        NetContext.get()
            .init(this) {
                errorMessage(newErrorMessage())
            }.addHostConfig {
                aipHandler(newApiHandler(errorHandler.get()))
                httpConfig(newHttpConfig(userManager.get(), appSettings.get(), errorHandler.get()))
                errorDataAdapter(newErrorDataAdapter())
                exceptionFactory { _, _ -> null }
            }
    }

    private fun configFoundation() {
        //安装 Activity/Fragment 注入器
        registerActivityLifecycleCallbacks(ComponentProcessor())
        //lib-base 配置
        AndroidSword.setDefaultFragmentContainerId(R.id.common_container_id) //默认的Fragment容器id
            .setDefaultFragmentAnimator(FragmentScaleAnimator())//Fragment切换动画
            .setDefaultPageStart(appSettings.get().defaultPageStart)//分页开始页码
            .setDefaultPageSize(appSettings.get().defaultPageSize)//默认分页大小
            .apply {
                //Dialog 最短展示时间
                minimumShowingDialogMills = appSettings.get().minimumDialogShowTime
                //默认的通用的LoadingDialog和Toast实现
                loadingViewFactory = { AppLoadingView(it) }
                //错误分类器
                errorClassifier = object : ErrorClassifier {
                    override fun isNetworkError(throwable: Throwable) =
                        throwable is NetworkErrorException || throwable is IOException

                    override fun isServerError(throwable: Throwable) =
                        throwable is ServerErrorException || throwable is HttpException && throwable.code() >= 500/*http code*/
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

}