package com.app.base.app

import android.app.Activity
import com.qmuiteam.qmui.util.QMUIStatusBarHelper


/** 设置状态栏黑色字体图标，返回 true 表示设置成功 */
fun Activity?.setStatusBarLightMode(): Boolean {
    return QMUIStatusBarHelper.setStatusBarLightMode(this)
}

/** 设置状态栏白色字体图标，返回 true 表示设置成功 */
@Suppress("UNUSED")
fun Activity?.setStatusBarDarkMode(): Boolean {
    return QMUIStatusBarHelper.setStatusBarDarkMode(this)
}
