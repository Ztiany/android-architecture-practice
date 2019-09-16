package me.ztiany.arch.home.main.mine.presentation

import android.os.Bundle
import android.widget.TextView
import com.android.base.app.mvvm.getViewModel
import com.app.base.app.InjectorBaseFragment

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-02 14:40
 */
class MineFragment : InjectorBaseFragment() {

    private val viewModule by lazy {
        getViewModel<MineViewModel>(viewModelFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    private fun subscribeViewModel() {

    }

    override fun provideLayout() = TextView(context).apply {
        text = "我的"
        textSize = 30F
    }

}