package me.ztiany.arch.home.main.middle

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.app.base.app.InjectorBaseFragment


/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-02 14:38
 */
class MiddleFragment : InjectorBaseFragment() {

    private val viewModule: MiddleViewModel by viewModels { viewModelFactory }

    override fun provideLayout() = TextView(context).apply {
        text = "陪伴"
        textSize = 30F
        gravity = Gravity.CENTER
        setTextColor(Color.BLUE)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

}