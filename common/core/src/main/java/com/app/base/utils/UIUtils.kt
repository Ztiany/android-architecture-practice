package com.app.base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.base.utils.android.compat.SystemBarCompat
import com.android.base.utils.android.views.textValue
import com.android.base.utils.common.isLengthIn
import com.app.base.R
import com.app.base.ui.R as UI_R
import com.google.android.material.color.MaterialColors
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import java.lang.ref.WeakReference

fun newAppStyleClickSpan(lifecycleOwner: LifecycleOwner, context: Context, onClick: () -> Unit): ClickableSpan {

    val listenerReference = WeakReference(onClick)
    val contextReference = WeakReference(context)

    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {

        private var saveOnClick: (() -> Unit)? = onClick
        private var saveContext: Context? = context

        override fun onDestroy(owner: LifecycleOwner) {
            saveOnClick = null
            saveContext = null
        }
    })

    return object : ClickableSpan() {
        override fun onClick(widget: View) {
            listenerReference.get()?.invoke()
        }

        override fun updateDrawState(ds: TextPaint) {
            contextReference.get()?.let {
                ds.color = MaterialColors.getColor(it, UI_R.attr.app_color_text_link, Color.BLACK)
            }
            ds.isUnderlineText = false
        }
    }

}

@SuppressLint("SetTextI18n")
fun EditText.enableIfMatchLength(target: View, min: Int, max: Int = Int.MAX_VALUE) {
    val textValue = textValue()
    target.isEnabled = isLengthIn(textValue, min, max)
    addTextChangedListener {
        target.isEnabled = isLengthIn(textValue(), min, max)
    }
}

@SuppressLint("SetTextI18n")
fun EditText.enableIfAtLength(target: View, length: Int) {
    val textValue = textValue()
    target.isEnabled = isLengthIn(textValue, length, length)
    addTextChangedListener {
        target.isEnabled = isLengthIn(textValue(), length, length)
    }
}

var supportStatusBarLightMode: Boolean = false
    private set

fun Activity.setNavigatorBarColorLightest() {
    SystemBarCompat.setNavigationBarColor(
        this,
        MaterialColors.getColor(this, UI_R.attr.app_color_lightest, "app_color_lightest not provided.")
    )
}

fun Activity.setNavigatorBarColorSameWithWindow() {
    SystemBarCompat.setNavigationBarColor(
        this,
        MaterialColors.getColor(this, android.R.attr.windowBackground, "windowBackground not provided.")
    )
}

/** 设置状态栏黑色字体图标，返回 true 表示设置成功 */
fun Activity.setStatusBarLightMode(): Boolean {
    return QMUIStatusBarHelper.setStatusBarLightMode(this).apply {
        supportStatusBarLightMode = this
    }
}

/** 设置状态栏白色字体图标，返回 true 表示设置成功 */
@Suppress("UNUSED")
fun Activity.setStatusBarDarkMode(): Boolean {
    return QMUIStatusBarHelper.setStatusBarDarkMode(this)
}
