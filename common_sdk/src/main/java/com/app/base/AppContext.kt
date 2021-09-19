package com.app.base

import android.content.Context
import androidx.multidex.MultiDex
import com.android.base.app.BaseAppContext
import com.android.base.app.ErrorClassifier
import com.android.base.app.Sword
import com.android.base.concurrent.DispatcherProvider
import com.android.base.concurrent.SchedulerProvider
import com.android.sdk.mediaselector.common.MediaSelectorConfiguration
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.NetworkErrorException
import com.android.sdk.net.core.exception.ServerErrorException
import com.android.sdk.net.core.service.ServiceFactory
import com.android.sdk.upgrade.AppUpgradeChecker
import com.app.base.app.*
import com.app.base.config.AppSettings
import com.app.base.services.usermanager.AppDataSource
import com.app.base.data.storage.StorageManager
import com.app.base.debug.DebugTools
import com.app.base.router.AppRouter
import com.app.base.upgrade.AppUpgradeInteractor
import com.app.base.widget.dialog.AppLoadingView
import com.blankj.utilcode.util.NetworkUtils
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

    @Inject lateinit var appDataSource: AppDataSource
    @Inject lateinit var errorHandler: ErrorHandler
    @Inject lateinit var serviceFactory: Lazy<ServiceFactory>
    @Inject lateinit var storageManager: Lazy<StorageManager>
    @Inject lateinit var schedulerProvider: SchedulerProvider
    @Inject lateinit var dispatcherProvider: DispatcherProvider
    @Inject lateinit var appRouter: AppRouter
    @Inject lateinit var appSettings: AppSettings

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        //设置
        appSettings.init()
        //路由
        appRouter.initRouter(this, true)
        //初始化网络库
        NetContext.get()
            .newBuilder()
            .aipHandler(newApiHandler(errorHandler))
            .httpConfig(newHttpConfig(appDataSource, appSettings, errorHandler))
            .networkChecker { NetworkUtils.isConnected() }
            //.postTransformer(null)
            .errorDataAdapter(newErrorDataAdapter())
            .errorMessage(newErrorMessage())
            .exceptionFactory { null }
            .setup()
        //调试
        DebugTools.init(this)
        //安装 Activity/Fragment 注入器
        registerActivityLifecycleCallbacks(AppComponentProcessor())
        //基础库配置
        Sword.setDefaultFragmentContainerId(R.id.common_container_id) //默认的Fragment容器id
            .setDefaultFragmentAnimator(FragmentScaleAnimator())//Fragment切换动画
            .setDefaultPageStart(appSettings.defaultPageStart)//分页开始页码
            .setDefaultPageSize(appSettings.defaultPageSize)//默认分页大小
            .setupRxJavaErrorHandler()
            .apply {
                //Dialog 最短展示时间
                minimumShowingDialogMills = appSettings.minimumDialogShowTime
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
        //ui
        MediaSelectorConfiguration.forceUseLegacyApi(true)
        AppUpgradeChecker.installInteractor(AppUpgradeInteractor(appSettings, serviceFactory.get()))
        //sdk
        //WeChatManager.initWeChatSDK(this, "", "")
        //Umeng.init(this)
        //Bugly.init(this)
        //PushManager.getInstance().init(this)
    }

}