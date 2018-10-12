package com.ztiany.architecture

import android.content.Context
import android.support.multidex.MultiDex
import com.android.base.app.BaseAppContext

/**
 *
 *@author Ztiany
 *      Date : 2018-09-06 17:14
 */
class ArchAppContext : BaseAppContext() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}