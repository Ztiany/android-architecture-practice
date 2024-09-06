package com.app.sample.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.android.base.utils.android.views.onThrottledClick
import com.app.base.app.AppBaseActivity
import com.app.sample.compose.ComposeSampleActivity
import me.ztiany.wan.sample.R
import me.ztiany.wan.sample.ViewArchSampleActivity

class SampleMainActivity : AppBaseActivity() {

    override fun provideLayout() = R.layout.sample_activity_main

    override fun setUpLayout(savedInstanceState: Bundle?) {
        findViewById<View>(R.id.sample_tv_view_arch).onThrottledClick {
            startActivity(Intent(this, ViewArchSampleActivity::class.java))
        }

        findViewById<View>(R.id.sample_tv_compose_arch).onThrottledClick {
            startActivity(Intent(this, ComposeSampleActivity::class.java))
        }
    }

}