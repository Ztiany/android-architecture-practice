package com.app.sample.view.epoxy

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.android.base.image.ImageLoaderFactory
import com.android.base.image.Source
import com.android.base.ui.banner.BannerViewPagerAdapter
import com.android.base.utils.android.views.newMMLayoutParams
import com.android.base.utils.android.views.onThrottledClick

internal class FeedBannerAdapter : BannerViewPagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = createBannerPagerView(container, context)
        val uri = entities[position]
        bindViewItem(item, uri, position)
        container.addView(item)
        return item
    }

    private fun createBannerPagerView(container: ViewGroup, context: Context): ImageView {
        return AppCompatImageView(container.context).apply {
            layoutParams = newMMLayoutParams()
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    private fun bindViewItem(imageView: ImageView, uri: Uri, position: Int) {
        ImageLoaderFactory.getImageLoader().display(imageView, Source.create(uri))
        imageView.onThrottledClick { callPagerClicked(position, imageView) }
    }

}