package me.ztiany.arch.home.main.index.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.android.base.app.mvvm.getViewModel
import com.app.base.app.InjectorBaseFragment

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-02 14:40
 */
class IndexFragment : InjectorBaseFragment() {

    private val viewModule by lazy {
        getViewModel<IndexViewModule>(viewModelFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    private fun subscribeViewModel() {

    }

    override fun provideLayout() = TextView(context).apply {
        text = "首页"
        textSize = 30F
        gravity = Gravity.CENTER
        setTextColor(Color.BLUE)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

}