package com.android.base

import android.content.Context
import android.content.res.Configuration
import com.android.base.architecture.app.ApplicationDelegate
import com.android.base.architecture.app.BaseAppContext
import com.android.base.architecture.fragment.animator.FragmentAnimator
import com.android.base.architecture.fragment.tools.FragmentConfig
import com.android.base.architecture.ui.list.Paging
import com.android.base.architecture.ui.list.RefreshLoadMoreViewFactory
import com.android.base.architecture.ui.list.RefreshLoadMoreViewFactory.Factory
import com.android.base.architecture.ui.list.RefreshViewFactory
import com.android.base.architecture.ui.loading.LoadingViewHost
import kotlinx.coroutines.flow.Flow

/**
 * A set of useful tools for android development, just like a sword.
 *
 * @author Ztiany
 */
object AndroidSword {

    /** Application lifecycle delegate */
    private val coreAppDelegate = ApplicationDelegate()

    ///////////////////////////////////////////////////////////////////////////
    // configuration
    ///////////////////////////////////////////////////////////////////////////
    /** 错误类型分类器 */
    var errorClassifier: ErrorClassifier? = null

    /** dialog 最小展示时间  */
    var minimumShowingDialogMills: Long = 0

    /** 用于创建 LoadingView*/
    var sLoadingViewHostFactory: ((Context) -> LoadingViewHost)? = null

    /** [Throwable] 到可读的 [CharSequence] 转换*/
    var errorConvert: ErrorConvert = object : ErrorConvert {
        override fun convert(throwable: Throwable): CharSequence {
            return throwable.message.toString()
        }
    }

    fun setCrashProcessor(crashProcessor: CrashProcessor): AndroidSword {
        coreAppDelegate.setCrashProcessor(crashProcessor)
        return this
    }

    fun setDefaultPageStart(pageStart: Int): AndroidSword {
        Paging.setDefaultPageStart(pageStart)
        return this
    }

    fun setDefaultPageSize(defaultPageSize: Int): AndroidSword {
        Paging.setDefaultPageSize(defaultPageSize)
        return this
    }

    /** 设置一个默认的布局 id，在使用 Fragments 中相关方法时，如果没有传入特定的容器 id  时，则使用设置的默认布局 id。  */
    fun setDefaultFragmentContainerId(defaultContainerId: Int): AndroidSword {
        FragmentConfig.setDefaultContainerId(defaultContainerId)
        return this
    }

    /**设置默认的 Fragment 转场动画*/
    fun setDefaultFragmentAnimator(animator: FragmentAnimator?): AndroidSword {
        FragmentConfig.setDefaultFragmentAnimator(animator)
        return this
    }

    fun registerRefreshLoadViewFactory(factory: Factory): AndroidSword {
        RefreshLoadMoreViewFactory.registerFactory(factory)
        return this
    }

    fun registerRefreshViewFactory(factory: RefreshViewFactory.Factory): AndroidSword {
        RefreshViewFactory.registerFactory(factory)
        return this
    }

    ///////////////////////////////////////////////////////////////////////////
    // lifecycle of application
    ///////////////////////////////////////////////////////////////////////////
    fun attachBaseContext(base: Context) {
        coreAppDelegate.attachBaseContext(base)
    }

    fun onCreate(baseAppContext: BaseAppContext) {
        coreAppDelegate.onCreate(baseAppContext)
    }

    fun onLowMemory() {
        coreAppDelegate.onLowMemory()
    }

    fun onTrimMemory(level: Int) {
        coreAppDelegate.onTrimMemory(level)
    }

    fun onConfigurationChanged(newConfig: Configuration) {
        coreAppDelegate.onConfigurationChanged(newConfig)
    }

    fun onTerminate() {
        coreAppDelegate.onTerminate()
    }

}

interface CrashProcessor {
    fun uncaughtException(thread: Thread, ex: Throwable)
}

interface ErrorClassifier {
    fun isNetworkError(throwable: Throwable): Boolean
    fun isServerError(throwable: Throwable): Boolean
}

interface ErrorConvert {
    fun convert(throwable: Throwable): CharSequence
}
