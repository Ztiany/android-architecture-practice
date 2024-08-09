package com.app.base.ui.widget.insets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.android.base.utils.android.compat.AndroidVersion
import com.android.base.utils.android.compat.SystemBarCompat
import com.android.base.utils.android.views.activityContext
import timber.log.Timber

/** 由于 App 设置顶部状态栏为白色，但是顶部状态栏的文字颜色在 6.0 后才支持设置为黑颜色，所以在不支持的的设备上需要加上一个灰色阴影，以防止用户看不到状态栏的颜色。 */
open class StatusHeightView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val realContext = activityContext
        if (AndroidVersion.atLeast(19) && realContext != null) {
            val statusBarHeight = SystemBarCompat.getStatusBarHeight(realContext)
            Timber.d("StatusBarHeightView set its height to $statusBarHeight. for $realContext.")
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