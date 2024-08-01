package com.android.sdk.mediaselector

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.sdk.mediaselector.processor.Processor
import com.android.sdk.mediaselector.processor.compress.CompressionConfig
import com.android.sdk.mediaselector.processor.compress.ImageCompressionProcessor
import com.android.sdk.mediaselector.processor.copy.CopyToAppSpecificProcessor

/**
 * 拷贝文件到应用私有目录。
 */
fun FragmentActivity.fileTransfer(): Processor {
    return CopyToAppSpecificProcessor(this, ActFragWrapper.create(this))
}

/**
 * 对图片进行压缩处理。
 */
fun FragmentActivity.imageCompressor(config: CompressionConfig = CompressionConfig()): Processor {
    return ImageCompressionProcessor(this, ActFragWrapper.create(this), config)
}

/**
 * 拷贝文件到应用私有目录。
 */
fun Fragment.fileTransfer(): Processor {
    return CopyToAppSpecificProcessor(this, ActFragWrapper.create(this))
}

/**
 * 对图片进行压缩处理。
 */
fun Fragment.imageCompressor(config: CompressionConfig = CompressionConfig()): Processor {
    return ImageCompressionProcessor(this, ActFragWrapper.create(this), config)
}
