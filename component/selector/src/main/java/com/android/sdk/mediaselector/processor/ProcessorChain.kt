package com.android.sdk.mediaselector.processor

import com.android.sdk.mediaselector.MediaItem

interface ProcessorChain {

    val scene: String

    fun onCanceled()

    fun onResult(items: List<MediaItem>)

}