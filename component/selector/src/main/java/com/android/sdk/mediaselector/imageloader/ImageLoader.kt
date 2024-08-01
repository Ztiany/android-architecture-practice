package com.android.sdk.mediaselector.imageloader

import android.content.Context
import android.widget.ImageView

interface ImageLoader {

    fun loadImage(context: Context, url: String, imageView: ImageView)

    fun loadImage(context: Context, imageView: ImageView, url: String, maxWidth: Int, maxHeight: Int)

    fun pauseRequests(context: Context)

    fun resumeRequests(context: Context)

}