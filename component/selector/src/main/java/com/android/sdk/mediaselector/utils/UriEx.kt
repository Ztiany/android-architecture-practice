package com.android.sdk.mediaselector.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.net.toFile
import com.android.sdk.mediaselector.MediaItem
import com.luck.picture.lib.utils.PictureFileUtils
import timber.log.Timber

internal data class FileAttribute(
    val name: String,
    val size: Long,
)

internal fun Uri.isFile(): Boolean {
    return scheme == "file"
}

internal fun Uri.getAttribute(context: Context): FileAttribute? {
    if (this.isFile()) {
        val file = toFile()
        return FileAttribute(file.name, file.length())
    }
    context.contentResolver.query(this, null, null, null, null, null)?.use {
        // moveToFirst() returns false if the cursor has 0 rows. Very handy for
        // "if there's anything to look at, look at it" conditionals.
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val displayName: String = it.getString(columnIndex)

            val sizeIndex: Int = it.getColumnIndex(OpenableColumns.SIZE)
            val size = if (!it.isNull(sizeIndex)) {
                it.getString(sizeIndex).toLongOrNull() ?: 0L
            } else 0L
            Timber.d("getAttribute: $displayName, $size")
            return FileAttribute(displayName, size)
        }
    }

    return null
}

internal fun Uri.getFilePostfix(context: Context): String? {
    return getAttribute(context)?.name?.substringAfterLast('.')?.lowercase().apply {
        Timber.d("getFilePostfix: uri = ${this@getFilePostfix}, value = $this")
    }
}

/**
 * refers to:
 *
 * - [get-real-path-from-uri-android-kitkat-new-storage-access-framework](https://stackoverflow.com/questions/20067508/get-real-path-from-uri-android-kitkat-new-storage-access-framework/20559175)
 * - [get-filename-and-path-from-uri-from-media-store](https://stackoverflow.com/questions/3401579/get-filename-and-path-from-uri-from-mediastore)
 * - [how-to-get-the-full-file-path-from-uri](https://stackoverflow.com/questions/13209494/how-to-get-the-full-file-path-from-uri)
 */
internal fun Uri.getAbsolutePath(context: Context): String? {
    return if (isFile()) {
        toFile().absolutePath
    } else {
        PictureFileUtils.getPath(context, Uri.parse(path))
    }
}

// need duration, size, width and height
internal fun MediaItem.tryFillPickedMediaInfo(context: Context): MediaItem {
    val fileAttribute = uri.getAttribute(context) ?: return this
    return this.copy(size = fileAttribute.size)
}