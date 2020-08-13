package com.app.base.utils

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.base.utils.android.views.colorFromId
import com.app.base.AppContext
import com.app.base.R
import java.lang.ref.WeakReference

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-05-25 11:12
 */
fun newAppStyleClickSpan(lifecycleOwner: LifecycleOwner, onClick: () -> Unit): ClickableSpan {

    val weakReference = WeakReference<() -> Unit>(onClick)

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
            ds.color = AppContext.get().colorFromId(R.color.colorPrimary)
            ds.isUnderlineText = false
        }
    }

}