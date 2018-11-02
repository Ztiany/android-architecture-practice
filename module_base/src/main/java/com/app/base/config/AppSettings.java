package com.app.base.config;

import android.app.Application;

import com.android.base.utils.android.SpCache;
import com.blankj.utilcode.util.AppUtils;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 11:03
 */
public class AppSettings {

    private AppSettings() {
        throw new UnsupportedOperationException();
    }

    private static final String SETTING_SP_NAME = "setting_sp_name";//spName
    private static final String LAST_VERSION = "last_version";//上一个版本
    private static final String IS_INSTALLED = "is_installed";//上一个版本

    private static SpCache mSharedPreferences;

    public synchronized static void init(Application application) {
        mSharedPreferences = new SpCache(application, SETTING_SP_NAME);
    }

    /**
     * 是不是第一次启动
     */
    @SuppressWarnings("unused")
    public static boolean isAppFirstTimeLaunch() {
        int last = mSharedPreferences.getInt(LAST_VERSION, -1);
        int curr = AppUtils.getAppVersionCode();
        if (last < curr) {
            mSharedPreferences.putInt(LAST_VERSION, curr);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAppAlreadyInstalled() {
        boolean isInstalled = mSharedPreferences.getBoolean(IS_INSTALLED, false);
        if (!isInstalled) {
            mSharedPreferences.putBoolean(IS_INSTALLED, true);
        }
        return mSharedPreferences.contains(LAST_VERSION) || isInstalled;
    }

    /**
     * 判断一个flag是否没有存储过
     *
     * @param flag 表示
     * @return true表示没有存储过
     */
    @SuppressWarnings("unused")
    public static boolean ifFirst(String flag) {
        boolean first = mSharedPreferences.getBoolean(flag, true);
        if (first) {
            mSharedPreferences.putBoolean(flag, false);
            return true;
        } else {
            return false;
        }
    }

}
