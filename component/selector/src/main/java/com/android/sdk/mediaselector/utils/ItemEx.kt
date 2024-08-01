package com.android.sdk.mediaselector.utils

import android.content.Context
import com.android.sdk.mediaselector.MediaItem
import com.luck.picture.lib.config.PictureMimeType
import timber.log.Timber

private val cropSupported = listOf(
    MineType.JPEG.value,
    MineType.JPG.value,
    MineType.PNG.value,
    MineType.WEBP.value,
)

private val imageCompressionSupported = listOf(
    MineType.IMAGE.value,
    MineType.JPEG.value,
    MineType.JPG.value,
    MineType.PNG.value,
    MineType.WEBP.value,
)

/**
 * Get the postfix of the file. Not include the dot.
 */
internal fun MediaItem.getPostfix(context: Context): String? {
    if (path.isNotEmpty()) {
        return path.substringAfterLast('.').lowercase()
    }
    return uri.getFilePostfix(context)
}

internal fun MediaItem.supportImageCompression(): Boolean {
    return (
            imageCompressionSupported.contains(mineType)
                    || PictureMimeType.isUrlHasImage(uri.toString())
                    || PictureMimeType.isUrlHasImage(path)
            ).apply {
            Timber.d("${this@supportImageCompression} supportImageCompression: $this")
        }
}

internal fun MediaItem.supportedCropping(context: Context): Boolean {
    // should read the file content to determine the file type, instead of relying on the file extension?
    if (cropSupported.contains(mineType)) {
        return true
    }
    val postfix = uri.getFilePostfix(context) ?: return false
    return cropSupported.any { it.contains(postfix) }
}