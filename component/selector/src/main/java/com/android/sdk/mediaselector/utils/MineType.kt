package com.android.sdk.mediaselector.utils

internal enum class MineType(val value: String) {
    IMAGE("image/*"),
    JPEG("image/jpeg"),
    JPG("image/jpg"),
    PNG("image/png"),
    WEBP("image/webp"),
    GIF("image/gif"),
    VIDEO("video/*"),
    MP4("video/mp4"),
    ALL("*/*");
}