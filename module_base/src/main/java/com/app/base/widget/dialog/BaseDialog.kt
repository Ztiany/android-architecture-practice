package com.app.base.widget.dialog

import android.content.Context
import android.support.v7.app.AppCompatDialog
import android.view.View
import com.app.base.R
import com.blankj.utilcode.util.ScreenUtils

open class BaseDialog(
        context: Context,
        private val limitHeight: Boolean = false,
        style: Int = R.style.Theme_Dialog_Common_Transparent_Floating
) : AppCompatDialog(context, style) {

    companion object {
        const val DEFAULT_WIDTH_SIZE_PERCENT = 0.75F
        const val DEFAULT_HEIGHT_SIZE_PERCENT = 0.8F
    }

    private lateinit var onLayoutChangeListener: View.OnLayoutChangeListener

    init {
        setCommonWindowAttributes()
    }

    private fun setCommonWindowAttributes() {
        window?.run {
            attributes.windowAnimations = R.style.Style_Anim_Dialog_Fade_In
        }
    }

    protected open var maxDialogWidthPercent = DEFAULT_WIDTH_SIZE_PERCENT
    protected open var maxDialogHeightPercent = DEFAULT_HEIGHT_SIZE_PERCENT

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