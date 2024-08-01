package com.android.sdk.mediaselector

import androidx.fragment.app.Fragment
import com.android.sdk.mediaselector.processor.Processor
import com.android.sdk.mediaselector.processor.compress.CompressionConfig
import com.android.sdk.mediaselector.processor.compress.ImageCompressionProcessor
import com.android.sdk.mediaselector.processor.copy.CopyToAppSpecificProcessor

fun Fragment.fileTransfer(): Processor {
    return CopyToAppSpecificProcessor(this, ActFragWrapper.create(this))
}

fun Fragment.imageCompressor(config: CompressionConfig = CompressionConfig()): Processor {
    return ImageCompressionProcessor(this, ActFragWrapper.create(this), config)
}
