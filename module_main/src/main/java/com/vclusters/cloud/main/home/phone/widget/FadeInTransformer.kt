package com.vclusters.cloud.main.home.phone.widget

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class FadeInTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        page.alpha = if (position < 0.0F) {
            1 + position * 0.6F
        } else {
            (1 - position) * 0.6F + 0.4F
        }
    }
    
}