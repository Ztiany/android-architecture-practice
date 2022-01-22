package com.app.base.widget.dialog

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import com.android.base.utils.android.WindowUtils
import com.app.base.R

open class BaseDialog(
        context: Context,
        private val limitHeight: Boolean = false,
        style: Int = R.style.ThemeDialogCommon_Transparent_Floating
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
            attributes.windowAnimations = R.style.StyleAnimDialogFadeIn
        }
    }

    protected open var maxDialogWidthPercent = DEFAULT_WIDTH_SIZE_PERCENT
    protected open var maxDialogHeightPercent = DEFAULT_HEIGHT_SIZE_PERCENT

    override fun show() {

        val screenWidth = WindowUtils.getScreenWidth()
        val screenHeight = WindowUtils.getScreenHeight()
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