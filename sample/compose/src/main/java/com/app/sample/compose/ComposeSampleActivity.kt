package com.app.sample.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import com.android.base.utils.android.compat.setLayoutExtendsToSystemBars
import com.app.base.app.AppBaseActivity
import com.app.base.compose.design.theme.EkAppTheme
import com.app.common.api.protocol.CustomizeSystemBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeSampleActivity : AppBaseActivity(), CustomizeSystemBar {

    override fun initialize(savedInstanceState: Bundle?) {
        setLayoutExtendsToSystemBars()
    }

    override fun setUpLayout(savedInstanceState: Bundle?) {
        setContent {
            EkAppTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                SampleNavGraph(startDestination = MAIN_SCREEN_ROUTE)
            }
        }
    }

}