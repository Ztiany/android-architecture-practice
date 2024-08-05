package com.android.sdk.mediaselector

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaItem(
    val id: String,
    val source: Source,
    val mineType: String,
    /** Available only when this item represent a video file. */
    val duration: Long = 0,

    val rawUri: Uri,
    val rawWidth: Int = 0,
    val rawHeight: Int = 0,
    val rawSize: Long = 0,
    /** As of Android 10, the path is no longer accessible. */
    val rawPath: String = "",

    val uri: Uri,
    val width: Int = 0,
    val height: Int = 0,
    val size: Long = 0,
    val path: String = "",

    /** attachment string */
    val attachmentMsg: String = "",
    /** attachment what */
    val attachmentWhat: Int = 0,
    /** attachment data */
    val attachmentData: Parcelable? = null,
) : Parcelable

enum class Source {
    Camera,
    Selector,
    MediaStore,
}
