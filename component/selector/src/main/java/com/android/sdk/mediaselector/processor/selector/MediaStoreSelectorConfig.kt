package com.android.sdk.mediaselector.processor.selector

import com.luck.picture.lib.config.SelectMimeType

internal class MediaStoreSelectorConfig(
    val mineType: Int = SelectMimeType.TYPE_IMAGE,
    val count: Int = 1,
    val imageAndVideo: Boolean = false,
    val showCamera: Boolean = false,
    val maxFileSize: Long = 0,
    val minFileSize: Long = 0,
    val maxDurationSecond: Int = 0,
    val minDurationSecond: Int = 0,
    val selectedData: List<String> = emptyList(),
    val includeGif: Boolean = false,
)