package com.app.sample.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import com.app.base.app.AppBaseActivity
import com.app.base.compose.theme.AppTheme
import com.app.base.ui.widget.insets.enableEdgeToEdgeCompatible
import com.app.common.api.protocol.CustomizeSystemBar

class ComposeSampleActivity : AppBaseActivity(), CustomizeSystemBar {

    override fun initialize(savedInstanceState: Bundle?) {
        enableEdgeToEdgeCompatible()
    }

    override fun setUpLayout(savedInstanceState: Bundle?) {
        setContent {
            AppTheme {
                Text(text = "I am a Text", fontSize = 40.sp)
            }
        }
    }

}