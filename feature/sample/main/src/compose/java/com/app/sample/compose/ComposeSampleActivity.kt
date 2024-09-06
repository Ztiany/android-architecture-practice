package com.app.sample.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import com.android.base.utils.android.compat.doBeforeSDK
import com.android.base.utils.android.compat.doFromSDK
import com.android.base.utils.android.compat.setLayoutExtendsToSystemBars
import com.android.base.utils.android.compat.setNavigationBarColor
import com.android.base.utils.android.compat.setNavigationBarLightMode
import com.android.base.utils.android.compat.setStatusBarLightMode
import com.android.base.utils.android.views.getStyledColor
import com.app.base.app.AppBaseActivity
import com.app.base.compose.theme.AppTheme
import com.app.common.api.protocol.CustomizeSystemBar

class ComposeSampleActivity : AppBaseActivity(), CustomizeSystemBar {

    override fun initialize(savedInstanceState: Bundle?) {
        setLayoutExtendsToSystemBars()
        setStatusBarLightMode()
        doFromSDK(26) {
            // Take effect as of API 26.
            setNavigationBarLightMode()
        }
        doBeforeSDK(26) {
            // Take effect as of API 21.
            setNavigationBarColor(getStyledColor(com.app.base.ui.theme.R.attr.app_color_deepest_opacity20, "app_color_deepest_opacity20"))
        }
    }

    override fun setUpLayout(savedInstanceState: Bundle?) {
        setContent {
            AppTheme {
                Text(text = "I am a Text", fontSize = 40.sp)
            }
        }
    }

}