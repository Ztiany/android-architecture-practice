package com.app.base.config

import com.android.base.utils.BaseUtils
import com.app.base.AppContext

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-30 15:08
 */
object AppSettings {

    val storage by lazy {
        AppContext.storageManager().newStorage("AppSettings", false)
    }

    const val DEFAULT_PAGE_START = 1

    const val DEFAULT_PAGE_SIZE = 20

    const val TRANSITION_ANIMATION_PHOTOS = "transition_animation_photos"

    const val MINIMUM_DIALOG_SHOWING_TIME = 500L

    val appFileProviderAuthorities: String
        get() = BaseUtils.getAppContext().packageName + ".file.provider"

    var supportStatusBarLightMode = false

}