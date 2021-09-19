package com.app.base.config

import com.android.base.utils.BaseUtils
import com.app.base.data.storage.StorageManager
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
    storageManager: StorageManager
) {

    private val initialized = AtomicBoolean(false)

    fun baseWebUrl(): String {
        return "http:dummy.com/api/"
    }

    fun baseApiUrl(): String {
        return "http:dummy.com/web/"
    }

    fun init() {
        if (initialized.compareAndSet(false, true)) {

        }
    }

    val storage by lazy {
        storageManager.newStorage("AppSettings", false)
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
            add(API_HOST, Environment("测试", "Test", "http://www.fake.com/api/"))
            add(H5_HOST, Environment("测试", "Test", "?"))
            add(WX_KEY, Environment("测试", "Test", "?"))
            add(ENV_VALUE, Environment("测试", "Test", "?"))

            add(API_HOST, Environment("生产", "Pro", "?"))
            add(H5_HOST, Environment("生产", "Pro", "?"))
            add(WX_KEY, Environment("生产", "Pro", "?"))
            add(ENV_VALUE, Environment("生产", "Pro", "?"))
        }
    }

}