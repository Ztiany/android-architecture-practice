package com.app.base.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import com.app.base.R
import com.blankj.utilcode.util.ScreenUtils

open class AppBaseDialog(
    context: Context,
    private val fixWidth: Boolean = true,
    private val limitHeight: Boolean = false,
    style: Int = R.style.AppTheme_Dialog_Common_Transparent_Floating
) : AppCompatDialog(context, style), AppDialogInterface {

    private lateinit var onLayoutChangeListener: View.OnLayoutChangeListener

    init {
        setCommonWindowAttributes()
    }

    private fun setCommonWindowAttributes() {
        window?.run {
            attributes.windowAnimations = R.style.AppAnimation_DialogFadeIn
        }
    }

    protected open val maxDialogWidthPercent: Float
        get() {
            return if (ScreenUtils.getScreenWidth() < ScreenUtils.getScreenHeight()) {
                0.75F
            } else {
                0.45F
            }
        }

    protected open val maxDialogHeightPercent: Float
        get() {
            return if (ScreenUtils.getScreenWidth() < ScreenUtils.getScreenHeight()) {
                0.7F
            } else {
                0.9F
            }
        }

    override fun show() {
        window?.run {
            if (fixWidth) {
                attributes = attributes.also {
                    val realScreenWidth = ScreenUtils.getScreenWidth()
                    it.width = (realScreenWidth * maxDialogWidthPercent).toInt()
                }
            }
        }

        if (limitHeight && !::onLayoutChangeListener.isInitialized) {
            onLayoutChangeListener = View.OnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
                val realScreenHeight = ScreenUtils.getScreenHeight()
                val maxHeight = (maxDialogHeightPercent * realScreenHeight).toInt()
                if (v.height > maxHeight) {
                    window?.attributes = window?.attributes?.also {
                        it.height = maxHeight
                    }
                }
            }
            window?.decorView?.addOnLayoutChangeListener(onLayoutChangeListener)
        }

        showCompat {
            super.show()
        }
    }

    override val dialog: Dialog
        get() = this

}