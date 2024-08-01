package com.android.sdk.mediaselector.processor.selector

import android.content.Context
import android.widget.ImageView
import com.android.sdk.mediaselector.getImageLoader
import com.luck.picture.lib.engine.ImageEngine

internal class InternalEngine : ImageEngine {

    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        getImageLoader().loadImage(context, url, imageView)
    }

    override fun loadImage(context: Context, imageView: ImageView, url: String, maxWidth: Int, maxHeight: Int) {
        getImageLoader().loadImage(context, imageView, url, maxWidth, maxHeight)
    }

    override fun loadAlbumCover(context: Context, url: String, imageView: ImageView) {
        getImageLoader().loadImage(context, url, imageView)
    }

    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        getImageLoader().loadImage(context, url, imageView)
    }

    override fun pauseRequests(context: Context) {
        getImageLoader().pauseRequests(context)
    }

    override fun resumeRequests(context: Context) {
        getImageLoader().resumeRequests(context)
    }

}