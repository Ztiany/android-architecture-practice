package com.app.base.ui.dialog

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import com.app.base.ui.dialog.dsl.DialogWindowSizeDescription

/**
 * Base implementation of ui-dialog. You can extend this class to create your own dialog.
 */
open class AppBaseDialog(
    context: Context,
    private val windowSize: DialogWindowSizeDescription = defaultWindowSize().toDialogWindowSizeDescription(),
    style: Int = com.app.base.ui.theme.R.style.AppTheme_Dialog_Common_Transparent_Floating,
) : AppCompatDialog(context, style) {

    private lateinit var onLayoutChangeListener: View.OnLayoutChangeListener

    init {
        setCommonWindowAttributes()
    }

    private fun setCommonWindowAttributes() {
        window?.run {
            attributes.windowAnimations = com.app.base.ui.theme.R.style.AppAnimation_DialogFadeIn
        }
    }

    override fun show() {
        // limit height
        if (!::onLayoutChangeListener.isInitialized) {
            onLayoutChangeListener = View.OnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
                val maxHeight = windowSize.getMaxHeight(context)
                if (v.height > maxHeight) {
                    window?.attributes = window?.attributes?.also {
                        it.height = maxHeight
                    }
                }
            }
            window?.decorView?.addOnLayoutChangeListener(onLayoutChangeListener)
        }

        showCompat {
            adjustWidth()
            super.show()
            //如果在 onCreate 中调用 setContentView，会导致设置的 window.attributes 失效。而调用 show 会触发 onCreate 方法的调用。
            adjustWidth()
        }
    }

    private fun adjustWidth() {
        window?.run {
            // fix width
            attributes = attributes.also {
                it.width = windowSize.getWidth(context)
            }
        }
    }

}