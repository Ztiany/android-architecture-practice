package com.android.sdk.mediaselector.utils

import android.content.Context
import com.android.sdk.mediaselector.MediaItem
import com.luck.picture.lib.config.PictureMimeType

internal fun MediaItem.getPostfix(context: Context): String? {
    if (path.isNotEmpty()) {
        return path.substringAfterLast('.').lowercase()
    }
    return uri.getFilePostfix(context)
}

internal fun MediaItem.supportImageCompression(): Boolean {
    return PictureMimeType.isUrlHasImage(uri.toString()) || PictureMimeType.isUrlHasImage(path)
}

private val cropSupported = listOf(
    MineType.JPEG.value,
    MineType.JPG.value,
    MineType.PNG.value,
    MineType.WEBP.value,
)

internal fun MediaItem.supportedCropping(context: Context): Boolean {
    // should read the file content to determine the file type, instead of relying on the file extension?
    if (cropSupported.contains(mineType)) {
        return true
    }
    val postfix = uri.getFilePostfix(context) ?: return false
    return cropSupported.any { it.contains(postfix) }
}