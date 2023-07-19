package com.app.base.config

import android.content.Context
import com.android.base.utils.BaseUtils
import com.app.base.BuildConfig
import com.app.base.data.storage.StorageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Ztiany
 */
@Singleton
class AppSettings @Inject internal constructor(
    @ApplicationContext private val context: Context, storageManager: StorageManager
) {

    private val initialized = AtomicBoolean(false)

    fun baseWebUrl(): String {
        return getBaseWebURL()
    }

    fun baseApiUrl(): String {
        return getBaseApiURL()
    }

    fun init() {
        if (initialized.compareAndSet(false, true)) {
            //如果规定了，选择某一个环境，则优先选择该环境
            initEnvironment()
            if (!"Auto".contentEquals(BuildConfig.specifiedHost)) {
                Timber.d("BuildConfig.specifiedHost =>%s", BuildConfig.specifiedHost);
                selectSpecified(BuildConfig.specifiedHost);
            }
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

    val isReleaseEnv: Boolean
        get() = BuildConfig.specifiedHost == "Pro"

    var agreeWithUserProtocol: Boolean
        set(value) {
            storage.putBoolean("agreeWithUserProtocol", value)
        }
        get() = storage.getBoolean("agreeWithUserProtocol", false)

    private fun selectSpecified(specifiedHost: String) {
        EnvironmentContext.select(API_HOST, specifiedHost)
        EnvironmentContext.select(H5_HOST, specifiedHost)
    }

    private fun getBaseApiURL(): String {
        return EnvironmentContext.selected(API_HOST).value
    }

    private fun getBaseWebURL(): String {
        return EnvironmentContext.selected(H5_HOST).value
    }

    companion object {
        internal const val API_HOST = "接口环境"
        internal const val H5_HOST = "H5 环境"
    }

    private fun initEnvironment() {
        EnvironmentContext.startEdit {
            add(API_HOST, Environment("生产", "Pro", "https://www.wanandroid.com/"))
            add(H5_HOST, Environment("生产", "Pro", "https://www.wanandroid.com/"))
        }
    }

}