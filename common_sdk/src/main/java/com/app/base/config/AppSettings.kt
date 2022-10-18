package com.app.base.config

import android.content.Context
import com.android.base.utils.BaseUtils
import com.app.base.data.storage.StorageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-30 15:08
 */
@Singleton
class AppSettings @Inject internal constructor(
    @ApplicationContext private val context: Context,
    storageManager: StorageManager
) {

    private val initialized = AtomicBoolean(false)

    fun baseWebUrl(): String {
        return "http://192.168.210.199/"
    }

    fun baseApiUrl(): String {
        return "http://192.168.210.199/api/"
    }

    fun init() {
        if (initialized.compareAndSet(false, true)) {

        }
    }

    val storage by lazy {
        storageManager.newStorage("AppSettings")
    }

    val defaultPageStart
        get() = 1

    val defaultPageSize
        get() = 20

    val transitionAnimationPhotos
        get() = "transition_animation_photos"

    val minimumDialogShowTime
        get() = 500L

    val appFileProviderAuthorities: String
        get() = BaseUtils.getAppContext().packageName + ".file.provider"

    /* todo: move it into native. */
    val aesKey: String = "xgj7adwbtia-ow7x"

    internal fun selectSpecified(specifiedHost: String) {
        EnvironmentContext.select(API_HOST, specifiedHost)
        EnvironmentContext.select(H5_HOST, specifiedHost)
        EnvironmentContext.select(WX_KEY, specifiedHost)
        EnvironmentContext.select(ENV_VALUE, specifiedHost)
    }

    internal fun getAPIBaseURL(): String {
        return EnvironmentContext.selected(API_HOST).value
    }

    internal fun getBaseWebURL(): String {
        return EnvironmentContext.selected(H5_HOST).value
    }

    fun getWxKey(): String {
        return EnvironmentContext.selected(WX_KEY).value
    }

    internal fun getEnvValuesPath(): String {
        return EnvironmentContext.selected(ENV_VALUE).value
    }

    companion object {
        internal const val API_HOST = "接口环境"
        internal const val H5_HOST = "H5环境"
        internal const val WX_KEY = "微信KEY"
        internal const val ENV_VALUE = "环境变量"
    }

    private fun initEnvironment() {
        EnvironmentContext.startEdit {
            add(API_HOST, Environment("测试1", "Test1", "http://demo.ysj.vclusters.com/"))
            add(H5_HOST, Environment("测试1", "Test1", "?"))
            add(WX_KEY, Environment("测试1", "Test1", "?"))
            add(ENV_VALUE, Environment("测试1", "Test1", "?"))

            add(API_HOST, Environment("测试2", "Test2", "http://192.168.210.199/api/"))
            add(H5_HOST, Environment("测试2", "Test2", "?"))
            add(WX_KEY, Environment("测试2", "Test2", "?"))
            add(ENV_VALUE, Environment("测试2", "Test2", "?"))

            add(API_HOST, Environment("生产", "Pro", "?"))
            add(H5_HOST, Environment("生产", "Pro", "?"))
            add(WX_KEY, Environment("生产", "Pro", "?"))
            add(ENV_VALUE, Environment("生产", "Pro", "?"))
        }
    }

}