package me.ztiany.arch.home.main.presentation.mine

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.android.base.app.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-02 14:40
 */
@AndroidEntryPoint
class MineFragment : BaseFragment() {

    private val viewModule: MineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    private fun subscribeViewModel() {

    }

    override fun provideLayout() = TextView(context).apply {
        text = "我的"
        textSize = 30F
        gravity = Gravity.CENTER
        setTextColor(Color.BLUE)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

}