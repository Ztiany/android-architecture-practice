package me.ztiany.arch.home.setting

import android.support.v7.widget.AppCompatTextView
import com.app.base.app.AppBaseActivity

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-08 19:13
 */
class SettingActivity : AppBaseActivity() {

    override fun layout() = AppCompatTextView(this).apply {
        text = "设置"
    }

}