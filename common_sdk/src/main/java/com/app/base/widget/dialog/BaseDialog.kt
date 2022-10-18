package com.app.base.widget.dialog

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import com.blankj.utilcode.util.ScreenUtils
import com.app.base.R

open class BaseDialog(
    context: Context,
    private val limitHeight: Boolean = false,
    style: Int = R.style.ThemeDialogCommon_Transparent_Floating
) : AppCompatDialog(context, style) {

    private lateinit var onLayoutChangeListener: View.OnLayoutChangeListener

    init {
        setCommonWindowAttributes()
    }

    private fun setCommonWindowAttributes() {
        window?.run {
            attributes.windowAnimations = R.style.StyleAnimDialogFadeIn
        }
    }

    protected open val maxDialogWidthPercent: Float
        get() {
            return if (ScreenUtils.getScreenWidth() < ScreenUtils.getScreenHeight()) {
                0.75F
            } else {
                0.4F
            }
        }

    protected open val maxDialogHeightPercent: Float
        get() {
            return if (ScreenUtils.getScreenWidth() < ScreenUtils.getScreenHeight()) {
                0.7F
            } else {
                0.8F
            }
        }

    override fun show() {

        val screenWidth = ScreenUtils.getScreenWidth()
        val screenHeight = ScreenUtils.getScreenHeight()
        val realScreenWidth = if (screenWidth < screenHeight) screenWidth else screenHeight
        val realScreenHeight = if (screenWidth < screenHeight) screenHeight else screenWidth

        window?.run {
            attributes = attributes.also {
                it.width = (realScreenWidth * maxDialogWidthPercent).toInt()
            }
        }

        if (limitHeight && !::onLayoutChangeListener.isInitialized) {
            onLayoutChangeListener = View.OnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
                val maxHeight = (maxDialogHeightPercent * realScreenHeight).toInt()
                if (v.height > maxHeight) {
                    window?.attributes = window?.attributes?.also {
                        it.height = maxHeight
                    }
                }
            }
            window?.decorView?.addOnLayoutChangeListener(onLayoutChangeListener)
        }

        super.show()
    }

}