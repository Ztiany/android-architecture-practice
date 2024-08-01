package com.android.sdk.mediaselector

interface ResultListener {

    fun onCanceled() {}

    fun onResult(scene: String, result: List<MediaItem>) {
        if (result.size == 1) {
            onSingleResult(scene, result.first())
        }
    }

    fun onSingleResult(scene: String, result: MediaItem) {}

}