package com.android.sdk.mediaselector.imageloader

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

internal class GlideImageLoader : ImageLoader {

    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .centerCrop()
            .into(imageView)
    }

    override fun loadImage(context: Context, imageView: ImageView, url: String, maxWidth: Int, maxHeight: Int) {
        Glide.with(context)
            .load(url)
            .override(maxWidth, maxHeight)
            .centerCrop()
            .into(imageView)
    }

    override fun pauseRequests(context: Context) {
        Glide.with(context).pauseRequests()
    }

    override fun resumeRequests(context: Context) {
        Glide.with(context).resumeRequests()
    }

}