package me.ztiany.arch.home.main.middle

import android.widget.TextView
import com.android.base.app.mvvm.getViewModel
import com.app.base.app.InjectorBaseFragment


/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-02 14:38
 */
class MiddleFragment : InjectorBaseFragment() {

    private val viewModule by lazy {
        getViewModel<MiddleViewModel>(viewModelFactory)
    }

    override fun provideLayout() = TextView(context).apply {
        text = "陪伴"
        textSize = 30F
    }

}