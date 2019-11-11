package com.app.base

import android.app.Activity
import android.content.Context
import androidx.multidex.MultiDex
import com.android.base.app.BaseAppContext
import com.android.base.app.ErrorClassifier
import com.android.base.app.Sword
import com.android.base.rx.SchedulerProvider
import com.android.sdk.net.exception.NetworkErrorException
import com.android.sdk.net.exception.ServerErrorException
import com.android.sdk.net.service.ServiceFactory
import com.app.base.app.AppSecurity
import com.app.base.app.ErrorHandler
import com.app.base.app.FragmentScaleAnimator
import com.app.base.config.AppSettings
import com.app.base.data.DataContext
import com.app.base.data.app.AppDataSource
import com.app.base.data.app.StorageManager
import com.app.base.debug.DebugTools
import com.app.base.router.AppRouter
import com.app.base.router.RouterManager
import com.app.base.widget.dialog.AppLoadingView
import com.app.base.widget.dialog.TipsManager
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import okhttp3.OkHttpClient
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 10:20
 */
abstract class AppContext : BaseAppContext(), HasAndroidInjector {

    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject lateinit var appDataSource: AppDataSource
    @Inject lateinit var errorHandler: ErrorHandler
    @Inject lateinit var serviceFactory: ServiceFactory
    @Inject lateinit var appRouter: AppRouter
    @Inject lateinit var storageManager: StorageManager
    @Inject lateinit var schedulerProvider: SchedulerProvider
    @Inject lateinit var okHttpClient: OkHttpClient

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        initApp()
    }

    //todo, initialization in multi thread.
    private fun initApp() {
        //安全层
        AppSecurity.init()
        //路由
        RouterManager.init(this)
        //数据层
        DataContext.init(this)
        //调试
        DebugTools.init(this)

        //完成全局对象注入
        injectAppContext()

        //基础库配置
        Sword.setDefaultFragmentContainerId(R.id.common_container_id) //默认的Fragment容器id
                .setDefaultFragmentAnimator(FragmentScaleAnimator())
                .setDefaultPageStart(AppSettings.DEFAULT_PAGE_START)//分页开始页码
                .setDefaultPageSize(AppSettings.DEFAULT_PAGE_SIZE)//默认分页大小
                .enableAutoInject()
                .setupRxJavaErrorHandler()
                .apply {
                    //Dialog 最短展示时间
                    minimumShowingDialogMills = 500
                    //默认的通用的LoadingDialog和Toast实现
                    loadingViewFactory = { AppLoadingView(it) }
                    //错误分类器
                    errorClassifier = object : ErrorClassifier {
                        override fun isNetworkError(throwable: Throwable) = throwable is NetworkErrorException || throwable is IOException
                        override fun isServerError(throwable: Throwable) = throwable is ServerErrorException || throwable is HttpException && throwable.code() >= 500
                    }
                }

        //给数据层设置全局数据源
        DataContext.getInstance().onAppDataSourcePrepared(appDataSource())
    }

    override fun androidInjector() = androidInjector

    protected abstract fun injectAppContext()

    open fun restartApp(activity: Activity) {
        TipsManager.showMessage("no implementation")
    }

    companion object {

        @JvmStatic
        private lateinit var context: AppContext

        @JvmStatic
        fun get(): AppContext {
            return context
        }

        @JvmStatic
        fun storageManager(): StorageManager {
            return context.storageManager
        }

        @JvmStatic
        fun errorHandler(): ErrorHandler {
            return context.errorHandler
        }

        @JvmStatic
        fun serviceFactory(): ServiceFactory {
            return context.serviceFactory
        }

        @JvmStatic
        fun appDataSource(): AppDataSource {
            return context.appDataSource
        }

        @JvmStatic
        fun appRouter(): AppRouter {
            return context.appRouter
        }

        @JvmStatic
        fun schedulerProvider(): SchedulerProvider {
            return context.schedulerProvider
        }

        @JvmStatic
        fun httpClient(): OkHttpClient {
            return context.okHttpClient
        }

    }

}