package com.app.base.widget.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.app.base.utils.supportStatusBarLightMode
import com.app.base.ui.R as UI_R

/** 由于 App 设置顶部状态栏为白色，但是顶部状态栏的文字颜色在 6.0 后才支持设置为黑颜色，所以在不支持的的设备上需要加上一个灰色阴影，以防止用户看不到状态栏的颜色。 */
class StatusInsetsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : StatusHeightView(context, attrs, defStyleAttr) {

    init {
        if (background == null) {
            background = if (supportStatusBarLightMode) {
                ColorDrawable(ContextCompat.getColor(context, UI_R.color.white))
            } else {
                ColorDrawable(ContextCompat.getColor(context, UI_R.color.opacity10_white))
            }
        }
    }

}