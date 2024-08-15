package com.app.base.ui.widget.insets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.android.base.utils.android.compat.AndroidVersion
import com.android.base.utils.android.compat.SystemBarCompat
import com.android.base.utils.android.views.activityContext
import timber.log.Timber

class StatusBarInsetStubView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val realContext = activityContext
        if (AndroidVersion.atLeast(19) && realContext != null) {
            val statusBarHeight = SystemBarCompat.getStatusBarHeight(realContext)
            Timber.d("StatusBarInsetStubView set its height to $statusBarHeight. for $realContext.")
            super.onMeasure(
                widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                    statusBarHeight, MeasureSpec.EXACTLY
                )
            )
        } else {
            super.onMeasure(widthMeasureSpec, 0)
        }
    }

}