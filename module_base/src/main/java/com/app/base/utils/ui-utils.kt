package com.app.base.utils

import android.annotation.SuppressLint
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.base.utils.android.views.getColorCompat
import com.android.base.utils.android.views.textValue
import com.android.base.utils.common.isLengthIn
import com.app.base.AppContext
import com.app.base.R
import java.lang.ref.WeakReference

fun newAppStyleClickSpan(lifecycleOwner: LifecycleOwner, onClick: () -> Unit): ClickableSpan {

    val weakReference = WeakReference(onClick)

    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {

        private var saveOnClick: (() -> Unit)? = onClick

        override fun onDestroy(owner: LifecycleOwner) {
            saveOnClick = null
        }
    })

    return object : ClickableSpan() {
        override fun onClick(widget: View) {
            weakReference.get()?.invoke()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = AppContext.get().getColorCompat(R.color.app_main)
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
