package com.app.sample.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.android.base.utils.android.compat.setLayoutExtendsToSystemBars
import com.app.base.app.AppBaseActivity

/**
 * @author Ztiany
 */
class SampleSplashActivity : AppBaseActivity() {

    override fun initialize(savedInstanceState: Bundle?) {
        setLayoutExtendsToSystemBars()
    }

    override fun setUpLayout(savedInstanceState: Bundle?) {
        //https://medium.com/@elye.project/three-important-yet-unbeknown-android-app-launcher-behaviors-part-2-139a4d88157
        //https://issuetracker.google.com/issues/72299534
        if (!isTaskRoot && intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intent.action != null && intent.action == Intent.ACTION_MAIN) {
            finish()
            return
        }

        startActivity(Intent(this, SampleMainActivity::class.java))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, 0)
        } else {
            overridePendingTransition(0, 0)
        }
        finish()
    }

}