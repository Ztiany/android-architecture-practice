package com.app.base.config

import com.android.sdk.cache.MMKVStorageImpl
import com.android.sdk.cache.Storage
import com.app.base.AppContext
import com.blankj.utilcode.util.AppUtils

/**
 * <pre>
 *  * 存储全局设置
 *  * 存储一些 UI 标记
</pre> *
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 11:03
 */
object AppSettings {

    const val DEFAULT_PAGE_START = 1
    const val DEFAULT_PAGE_SIZE = 20
    const val TRANSITION_ANIMATION_PHOTOS = "transition_animation_photos"

    fun appFileProviderAuthorities() = AppContext.get().packageName + ".file.provider"

    private const val STABLE_SETTING_SP_NAME = "stable_setting_sp_name"

    private var sStorage: Storage = MMKVStorageImpl(AppContext.get(), STABLE_SETTING_SP_NAME, false)

    private var sSupportStatusBarLightMode = false

    /**
     * 相同版本的 app 是不是第一次启动
     */
    fun isFirstInSameVersion(flag: String): Boolean {
        val last = sStorage!!.getInt(flag, -1)
        val curr = AppUtils.getAppVersionCode()
        if (last < curr) {
            sStorage!!.putInt(flag, curr)
            return true
        } else {
            return false
        }
    }

    /**
     * 判断一个 flag 是否没有存储过
     *
     * @param flag 表示
     * @return true表示没有存储过
     */
    fun isFirst(flag: String): Boolean {
        val first = settingsStorage()!!.getBoolean(flag, true)
        if (first) {
            settingsStorage()!!.putBoolean(flag, false)
            return true
        } else {
            return false
        }
    }

    fun setSupportStatusBarLightMode(supportStatusBarLightMode: Boolean) {
        sSupportStatusBarLightMode = supportStatusBarLightMode
    }

    fun supportStatusBarLightMode(): Boolean {
        return sSupportStatusBarLightMode
    }

    fun settingsStorage(): Storage {
        return sStorage
    }

}