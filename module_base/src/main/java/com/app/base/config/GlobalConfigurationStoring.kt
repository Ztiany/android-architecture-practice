package com.app.base.config

import com.android.sdk.cache.Storage
import com.app.base.data.app.AppDataSource
import com.app.base.data.app.StorageManager
import com.blankj.utilcode.util.AppUtils
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-03-30 14:47
 */
@Singleton
class GlobalConfigurationStoring @Inject constructor(
        storageManager: StorageManager,
        private val appDataSource: AppDataSource
) {

    //========================================================================================
    // 通用
    //========================================================================================
    private var storage: Storage = storageManager.newStorage("module_related_config_manager")

    /**
     * 判断一个 flag 是否没有存储过
     *
     * @param flag 表示
     * @return true表示没有存储过
     */
    fun isFirst(flag: String): Boolean {
        val first = storage.getBoolean(flag, true)
        return if (first) {
            storage.putBoolean(flag, false)
            true
        } else {
            false
        }
    }

    /**
     * 相同版本的 app 是不是第一次启动
     */
    fun isFirstInSameVersion(flag: String): Boolean {
        val last = storage.getInt(flag, -1)
        val curr = AppUtils.getAppVersionCode()
        return if (last < curr) {
            storage.putInt(flag, curr)
            true
        } else {
            false
        }
    }

    //========================================================================================
    // 是否展示隐私协议
    //========================================================================================
    var needShowPrivacyProtocolDialog: Boolean
        get() {
            return storage.getBoolean("launcher_user_privacy_dialog_had_showed_${AppUtils.getAppVersionName()}", true)
        }
        set(value) {
            storage.putBoolean("launcher_user_privacy_dialog_had_showed_${AppUtils.getAppVersionName()}", value)
        }

}