package com.android.sdk.mediaselector.imageloader

import android.content.Context
import android.widget.ImageView
import com.android.base.image.DisplayConfig
import com.android.base.image.ImageLoaderFactory

internal class GlideImageLoader : ImageLoader {

    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        ImageLoaderFactory.getImageLoader().display(imageView, url)
    }

    override fun loadImage(context: Context, imageView: ImageView, url: String, maxWidth: Int, maxHeight: Int) {
        ImageLoaderFactory.getImageLoader().display(imageView, url, DisplayConfig.create().setSize(maxWidth, maxHeight))
    }

    override fun pauseRequests(context: Context) {
        ImageLoaderFactory.getImageLoader().pause(context)
    }

    override fun resumeRequests(context: Context) {
        ImageLoaderFactory.getImageLoader().resume(context)
    }

}