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
    @ApplicationContext private val context: Context,
    storageManager: StorageManager,
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
            if (!"Auto".contentEquals(BuildConfig.appHostFlag)) {
                Timber.d("BuildConfig.appHostFlag =>%s", BuildConfig.appHostFlag);
                EnvironmentContext.allCategory().forEach { (category, _) ->
                    EnvironmentContext.select(category, BuildConfig.appHostFlag)
                }
            }
        }
    }

    internal val storage by lazy {
        storageManager.newStorage("AppSettings")
    }

    val defaultPageStart
        get() = 1

    val defaultPageSize
        get() = 20

    val minimumDialogShowTime
        get() = 500L

    val fileProviderAuthorities: String
        get() = BaseUtils.getAppContext().packageName + ".file.provider"

    val isReleaseEnv: Boolean = BuildConfig.appHostFlag == "Pro"

    var agreeWithUserProtocol: Boolean
        set(value) {
            storage.putBoolean("agreeWithUserProtocol", value)
        }
        get() = storage.getBoolean("agreeWithUserProtocol", false)

    private fun getBaseApiURL(): String {
        return EnvironmentContext.selected(API_HOST).value
    }

    private fun getBaseWebURL(): String {
        return EnvironmentContext.selected(H5_HOST).value
    }

    companion object {
        internal const val API_HOST = "API-Address"
        internal const val H5_HOST = "H5-Address"
    }

    private fun initEnvironment() {
        EnvironmentContext.startEdit {
            add(API_HOST, EnvironmentItem("生产", "Pro", "http://tools.cretinzp.com/jokes/"))
            add(H5_HOST, EnvironmentItem("生产", "Pro", "http://tools.cretinzp.com/jokes/"))
        }
    }

}