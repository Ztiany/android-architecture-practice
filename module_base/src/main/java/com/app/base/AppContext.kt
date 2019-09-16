package com.app.base

import android.app.Activity
import android.content.Context
import android.support.multidex.MultiDex
import androidx.fragment.app.Fragment
import com.android.base.app.BaseAppContext
import com.android.base.app.BaseKit
import com.android.base.rx.SchedulerProvider
import com.android.sdk.net.exception.NetworkErrorException
import com.android.sdk.net.exception.ServerErrorException
import com.android.sdk.net.service.ServiceFactory
import com.app.base.app.AppSecurity
import com.app.base.app.ErrorHandler
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
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import okhttp3.OkHttpClient
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 10:20
 */
abstract class AppContext : BaseAppContext(), HasSupportFragmentInjector, HasActivityInjector {

    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
    @Inject lateinit var dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject lateinit var mAppDataSource: AppDataSource
    @Inject lateinit var mErrorHandler: ErrorHandler
    @Inject lateinit var mServiceFactory: ServiceFactory
    @Inject lateinit var mAppRouter: AppRouter
    @Inject lateinit var mStorageManager: StorageManager
    @Inject lateinit var mSchedulerProvider: SchedulerProvider
    @Inject lateinit var mOkHttpClient: OkHttpClient

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
        BaseKit.get()
                .enableAutoInject()
                .setDefaultFragmentContainerId(R.id.common_container_id)//默认的Fragment容器id
                .registerLoadingFactory { AppLoadingView(it) }//默认的通用的LoadingDialog和Toast实现
                .setDefaultPageStart(AppSettings.DEFAULT_PAGE_START)//分页开始页码
                .setDefaultPageSize(AppSettings.DEFAULT_PAGE_SIZE)//默认分页大小
                .setErrorClassifier(object : BaseKit.ErrorClassifier {
                    override fun isNetworkError(throwable: Throwable): Boolean {
                        Timber.d("isNetworkError $throwable")
                        return throwable is NetworkErrorException || throwable is IOException
                    }

                    override fun isServerError(throwable: Throwable): Boolean {
                        Timber.d("isServerError $throwable")
                        return throwable is ServerErrorException || throwable is HttpException && throwable.code() >= 500
                    }
                })

        //给数据层设置全局数据源
        //DataContext.getInstance().onAppDataSourcePrepared(appDataSource());
    }

    override fun activityInjector() = dispatchingActivityInjector

    override fun supportFragmentInjector() = dispatchingFragmentInjector

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
            return context.mStorageManager
        }

        @JvmStatic
        fun errorHandler(): ErrorHandler {
            return context.mErrorHandler
        }

        @JvmStatic
        fun serviceFactory(): ServiceFactory {
            return context.mServiceFactory
        }

        @JvmStatic
        fun appDataSource(): AppDataSource {
            return context.mAppDataSource
        }

        @JvmStatic
        fun appRouter(): AppRouter {
            return context.mAppRouter
        }

        @JvmStatic
        fun schedulerProvider(): SchedulerProvider {
            return context.mSchedulerProvider
        }

        @JvmStatic
        fun httpClient(): OkHttpClient {
            return context.mOkHttpClient
        }

    }

}