package com.android.sdk.mediaselector.processor.selector

import com.luck.picture.lib.config.SelectMimeType

internal class GalleryPickerConfig(
    val mineType: Int = SelectMimeType.TYPE_IMAGE,
    val maxFileSize: Long = 0,
    val minFileSize: Long = 0,
    val maxDurationSecond: Int = 0,
    val minDurationSecond: Int = 0,
)
