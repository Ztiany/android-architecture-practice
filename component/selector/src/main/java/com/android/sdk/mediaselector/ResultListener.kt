package com.android.sdk.mediaselector

interface ResultListener {

    fun onCanceled() {}

    fun onResult(result: List<MediaItem>) {
        if (result.size == 1) {
            onSingleResult(result.first())
        }
    }

    fun onSingleResult(result: MediaItem) {}

}