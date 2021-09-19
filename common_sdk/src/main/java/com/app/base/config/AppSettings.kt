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

}