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
import com.app.base.ui.theme.R
import com.google.android.material.color.MaterialColors
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
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
                ds.color = MaterialColors.getColor(it, R.attr.app_color_text_link, Color.BLACK)
            }
            ds.isUnderlineText = false
        }
    }

}

@SuppressLint("SetTextI18n")
fun EditText.enableTargetViewIfMatchLength(target: View, min: Int, max: Int = Int.MAX_VALUE) {
    target.isEnabled = isLengthIn(textValue(), min, max)
    addTextChangedListener {
        target.isEnabled = isLengthIn(textValue(), min, max)
    }
}

@SuppressLint("SetTextI18n")
fun EditText.enableTargetViewIfAtLength(target: View, length: Int) {
    target.isEnabled = isLengthIn(textValue(), length, length)
    addTextChangedListener {
        target.isEnabled = isLengthIn(textValue(), length, length)
    }
}

@SuppressLint("SetTextI18n")
fun EditText.enableTargetViewIfNotEmpty(target: View, length: Int) {
    target.isEnabled = textValue().isNotEmpty()
    addTextChangedListener {
        target.isEnabled = textValue().isNotEmpty()
    }
}

fun Activity.setNavigationBarColorLightest() {
    SystemBarCompat.setNavigationBarColor(
        this,
        MaterialColors.getColor(this, R.attr.app_color_lightest, "app_color_lightest not provided.")
    )
}

fun Activity.setNavigationBarColorSameWithWindow() {
    SystemBarCompat.setNavigationBarColor(
        this,
        MaterialColors.getColor(this, android.R.attr.windowBackground, "windowBackground not provided.")
    )
}

fun Activity.setStatusBarColorLightest() {
    SystemBarCompat.setStatusBarColor(
        this,
        MaterialColors.getColor(this, R.attr.app_color_lightest, "app_color_lightest not provided.")
    )
}

fun Activity.setStatusBarColorSameWithWindow() {
    SystemBarCompat.setStatusBarColor(
        this,
        MaterialColors.getColor(this, android.R.attr.windowBackground, "windowBackground not provided.")
    )
}

