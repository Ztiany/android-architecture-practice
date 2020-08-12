package me.ztiany.arch.home.main.presentation.middle

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.android.base.app.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.arch.home.main.MainFragment


/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-02 14:38
 */
@AndroidEntryPoint
class MiddleFragment : BaseFragment(), MainFragment.MainFragmentChild {

    private val viewModule: MiddleViewModel by viewModels()

    override fun provideLayout() = TextView(context).apply {
        text = "中间"
        textSize = 30F
        gravity = Gravity.CENTER
        setTextColor(Color.BLUE)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

}