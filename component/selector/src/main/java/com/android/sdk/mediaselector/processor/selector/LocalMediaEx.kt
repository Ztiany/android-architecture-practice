package com.android.sdk.mediaselector.processor.selector

import android.net.Uri
import com.android.sdk.mediaselector.MediaItem
import com.android.sdk.mediaselector.Source
import com.android.sdk.mediaselector.utils.isFileUri
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import java.io.File


internal fun LocalMedia.toItem(fromGallery: Boolean): MediaItem {
    val rawUri = if (PictureMimeType.isContent(path) || path.isFileUri()) {
        Uri.parse(path)
    } else {
        Uri.fromFile(File(path))
    }

    return MediaItem(
        id = id.toString(),
        source = if (fromGallery) Source.Selector else if (isCameraSource) Source.Camera else Source.MediaStore,
        mineType = mimeType,
        duration = duration,
        // final
        uri = rawUri,
        path = path,
        width = width,
        height = height,
        size = size,
        // raw
        rawUri = rawUri,
        rawPath = path,
        rawSize = size,
        rawWidth = width,
        rawHeight = height,
    )
}