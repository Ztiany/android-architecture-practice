package com.android.sdk.mediaselector

internal interface ResultListener {

    fun onCanceled()

    fun onResult(scene: String, items: List<MediaItem>)

}